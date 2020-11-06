;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns cognitect.aws.client.api.async
  "API functions for using a client to interact with AWS services."
  (:require [clojure.core.async :as a]
            [cognitect.aws.client :as client]
            [cognitect.aws.retry :as retry]
            [cognitect.aws.service :as service]))

(defn ^:skip-wiki validate-requests?
  "For internal use. Don't call directly."
  [client]
  (some-> client client/-get-info :validate-requests? deref))

(defn ^:skip-wiki validate-requests
  "For internal use. Don't call directly."
  [client tf]
  (reset! (-> client client/-get-info :validate-requests?) tf)
  (when tf
    (service/load-specs (-> client client/-get-info :service)))
  tf)

(defn invoke
  "Async version of cognitect.aws.client.api/invoke. Returns
  a core.async channel which delivers the result.

  Additional supported keys in op-map:

  :ch - optional, channel to deliver the result

  Alpha. Subject to change."
  [client op-map]
  (let [result-chan                          (or (:ch op-map) (a/promise-chan))
        {:keys [service retriable? backoff]} (client/-get-info client)]
    (when-not (contains? (:operations service) (:op op-map))
      (throw (ex-info "Operation not supported" {:service   (keyword (service/service-name service))
                                                 :operation (:op op-map)})))
    (retry/with-retry
      #(client/send-request client op-map)
      result-chan
      (or (:retriable? op-map) retriable?)
      (or (:backoff op-map) backoff))
    result-chan))
