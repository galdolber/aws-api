;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns cognitect.aws.client.validate
  "API functions for using a client to interact with AWS services."
  (:require [cognitect.aws.client.doc :as doc]
            [cognitect.aws.dynaload :as dynaload]))

(defn request-spec-key
  "Returns the key for the request spec for op.

  Alpha. Subject to change."
  [client op]
  (doc/request-spec-key (-> client :service) op))

(defn response-spec-key
  "Returns the key for the response spec for op.

  Alpha. Subject to change."
  [client op]
  (doc/response-spec-key (-> client :service) op))

(defn ^:skip-wiki validate-requests?
  "For internal use. Don't call directly."
  [client]
  (some-> client :validate-requests? deref))

(defn ^:skip-wiki validate-requests
  "For internal use. Don't call directly."
  [client tf]
  (reset! (-> client :validate-requests?) tf)
  (when tf
    (doc/load-specs (-> client :service)))
  tf)

(def ^:private registry-ref (delay (dynaload/load-var 'clojure.spec.alpha/registry)))
(defn ^:skip-wiki registry
  "For internal use. Don't call directly."
  [& args] (apply @registry-ref args))

(def ^:private valid?-ref (delay (dynaload/load-var 'clojure.spec.alpha/valid?)))
(defn ^:skip-wiki valid?
  "For internal use. Don't call directly."
  [& args] (apply @valid?-ref args))

(def ^:private explain-data-ref (delay (dynaload/load-var 'clojure.spec.alpha/explain-data)))
(defn ^:skip-wiki explain-data
  "For internal use. Don't call directly."
  [& args] (apply @explain-data-ref args))

(defn ^:skip-wiki validate
  "For internal use. Don't call directly."
  [service {:keys [op request] :or {request {}}}]
  (let [spec (doc/request-spec-key service op)]
    (when (contains? (-> (registry) keys set) spec)
      (when-not (valid? spec request)
        (assoc (explain-data spec request)
               :cognitect.anomalies/category :cognitect.anomalies/incorrect)))))
