;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns cognitect.aws.client.api
  "API functions for using a client to interact with AWS services."
  (:require [cognitect.aws.client :as client]
            [cognitect.aws.endpoint :as endpoint]
            [cognitect.aws.service :as service]
            [cognitect.aws.credentials :as credentials]
            [cognitect.aws.region :as region]
            [cognitect.aws.signing]
            [cognitect.aws.signing.impl]
            [cognitect.aws.protocols.rest-json]
            [cognitect.aws.protocols.rest-xml]))

(defn client
  "Given a config map, create a client for specified api. Supported keys:

  :api                  - required, this or api-descriptor required, the name of the api
                          you want to interact with e.g. :s3, :cloudformation, etc
  :http-client          - optional, to share http-clients across aws-clients.
                          See default-http-client.
  :region-provider      - optional, implementation of aws-clojure.region/RegionProvider
                          protocol, defaults to cognitect.aws.region/default-region-provider.
                          Ignored if :region is also provided
  :region               - optional, the aws region serving the API endpoints you
                          want to interact with, defaults to region provided by
                          by the region-provider
  :credentials-provider - optional, implementation of
                          cognitect.aws.credentials/CredentialsProvider
                          protocol, defaults to
                          cognitect.aws.credentials/default-credentials-provider
  :endpoint-override    - optional, map to override parts of the endpoint. Supported keys:
                            :protocol     - :http or :https
                            :hostname     - string
                            :port         - int
                            :path         - string
                          If the hostname includes an AWS region, be sure use the same
                          region for the client (either via out of process configuration
                          or the :region key supplied to this fn).
                          Also supports a string representing just the hostname, though
                          support for a string is deprectated and may be removed in the
                          future.

  Alpha. Subject to change."
  [{:keys [api region region-provider credentials-provider endpoint-override http-client service]
    :or   {endpoint-override {}}}]
  (let [region-provider
        (cond region          (reify region/RegionProvider (fetch [_] region))
              region-provider region-provider
              :else
              (region/default-region-provider http-client))
        credentials-provider
        (or credentials-provider
            (credentials/default-credentials-provider http-client))
        endpoint-provider
        (endpoint/default-endpoint-provider
         api
         (get-in service [:metadata :endpointPrefix])
         endpoint-override)]
    {:service service
     :http-client http-client
     :endpoint-provider endpoint-provider
     :region-provider region-provider
     :credentials-provider credentials-provider
     :validate-requests? (atom nil)}))

(defn invoke
  "Package and send a request to AWS and return the result.

  Supported keys in op-map:

  :op                   - required, keyword, the op to perform
  :request              - required only for ops that require them.

  Alpha. Subject to change."
  [client op-map]
  (let [{:keys [service]} client]
    (when-not (contains? (:operations service) (:op op-map))
      (throw (ex-info "Operation not supported" {:service   (keyword (service/service-name service))
                                                 :operation (:op op-map)})))
    (client/send-request client op-map)))
