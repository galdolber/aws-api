;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns ^:skip-wiki cognitect.aws.protocols.rest-xml
  "Impl, don't call directly."
  (:require [cognitect.aws.shape :as shape]
            [cognitect.aws.protocols.common :as common]
            [cognitect.aws.protocols.rest :as rest]))

(defn build [{:keys [shapes operations metadata] :as service} op-map]
  (rest/build-http-request service
                           op-map
                           (fn [shape-name shape data]
                             (when data
                               (shape/xml-serialize shape
                                                    data
                                                    (or (:locationName shape) shape-name))))))

(defn parse [service op-map http-response]
  (rest/parse-http-response service
                            op-map
                            http-response
                            shape/xml-parse
                            common/xml-parse-error))
