;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns ^:skip-wiki cognitect.aws.client
  "Impl, don't call directly."
  (:require [cognitect.aws.util :as util]
            [cognitect.aws.interceptors :as interceptors]
            [cognitect.aws.endpoint :as endpoint]
            [cognitect.aws.region :as region]
            [cognitect.aws.credentials :as credentials]
            [cognitect.aws.protocols.query :as query]
            [cognitect.aws.protocols.json :as json]
            [cognitect.aws.protocols.rest-json :as rest-json]
            [cognitect.aws.protocols.rest-xml :as rest-xml]
            [cognitect.aws.protocols.ec2 :as ec2]))

(set! *warn-on-reflection* true)

(defn build-http-request [service op-map]
  (let [protocol (get-in service [:metadata :protocol])]
    (case protocol
      "json" (json/build service op-map)
      "rest-json" (rest-json/build service op-map)
      "rest-xml" (rest-xml/build service op-map)
      "query" (query/build service op-map)
      "ec2" (ec2/build service op-map)
      (throw (ex-info "Protocol not supported" {:protocol protocol})))))

(defn parse-http-response [service op-map http-response]
  (let [protocol (get-in service [:metadata :protocol])]
    (case protocol
      "json" (json/parse service op-map http-response)
      "rest-json" (rest-json/parse service op-map http-response)
      "rest-xml" (rest-xml/parse service op-map http-response)
      "query" (query/parse service op-map http-response)
      "ec2" (ec2/parse service op-map http-response)
      (throw (ex-info "Protocol not supported" {:protocol protocol})))))

(defmulti sign-http-request
  "Sign the HTTP request."
  (fn [service endpoint credentials http-request]
    (get-in service [:metadata :signatureVersion])))

;; TODO convey throwable back from impl
(defn ^:private handle-http-response
  [service op-map http-response]
  (try
    (if (:cognitect.anomalies/category http-response)
      http-response
      (parse-http-response service op-map http-response))
    (catch Throwable t
      {:cognitect.anomalies/category :cognitect.anomalies/fault
       ::throwable t})))

(defn ^:private with-endpoint [req {:keys [protocol
                                           hostname
                                           port
                                           path]}]
  (cond-> (-> req
              (assoc-in [:headers "host"] hostname)
              (assoc :server-name hostname))
    protocol (assoc :scheme protocol)
    port     (assoc :server-port port)
    path     (assoc :uri path)))

(defn ^:private build-throwable [t response-meta op-map]
  (with-meta
    {:cognitect.anomalies/category :cognitect.anomalies/fault
     ::throwable t}
    (swap! response-meta
           assoc :op-map op-map)))

(defn send-request
  "For internal use. Send the request to AWS and return a channel which delivers the response.

  Alpha. Subject to change."
  [client op-map]
  (let [{:keys [service http-client region-provider credentials-provider endpoint-provider]} client
        response-meta (atom {})
        region (region/fetch region-provider)
        creds (credentials/fetch credentials-provider)
        endpoint (endpoint/fetch endpoint-provider region)]
    (try
      (let [http-request (-> (build-http-request service op-map)
                             (with-endpoint endpoint)
                             (update :body util/->bbuf)
                             ((partial interceptors/modify-http-request service op-map)))
            http-request (sign-http-request service endpoint creds http-request)]
        (swap! response-meta assoc :http-request http-request)
        (let [response (http-client http-request)]
          (with-meta
            (handle-http-response service op-map response)
            (swap! response-meta assoc
                   :http-response (update response :body util/->input-stream)))))
      (catch Throwable t
        (build-throwable t response-meta op-map)))))
