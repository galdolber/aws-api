;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns ^:skip-wiki cognitect.aws.service
  "Impl, don't call directly."
  (:require [clojure.string :as str]
            [cognitect.aws.shape :as shape]))

(def base-ns "cognitect.aws")

(def base-resource-path "cognitect/aws")

(defn shape
  "Returns the shape referred by `shape-ref`."
  [service shape-ref]
  (shape/with-resolver (select-keys service [:shapes]) shape-ref))

(defn endpoint-prefix
  [service]
  (get-in service [:metadata :endpointPrefix]))

(defn signing-name
  [service]
  (get-in service [:metadata :signingName]))

(defn service-name [service]
  (-> service :metadata :uid
      (str/replace #"-\d{4}-\d{2}-\d{2}" "")
      (str/replace #"\s" "-")
      (str/replace #"\." "-")))

(defn ns-prefix
  "Returns the namespace prefix to use when looking up resources."
  [service]
  (format "%s.%s" base-ns (service-name service)))
