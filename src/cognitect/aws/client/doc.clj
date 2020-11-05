(ns cognitect.aws.client.doc
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]
            [cognitect.aws.client.api :as api]))

(defn doc-str
  "Given data produced by `ops`, returns a string
  representation.

  Alpha. Subject to change."
  [{:keys [documentation documentationUrl request required response refs] :as doc}]
  (when doc
    (str/join "\n"
              (cond-> ["-------------------------"
                       (:name doc)
                       ""
                       documentation]
                documentationUrl
                (into [""
                       documentationUrl])
                request
                (into [""
                       "-------------------------"
                       "Request"
                       ""
                       (with-out-str (pp/pprint request))])
                required
                (into ["Required"
                       ""
                       (with-out-str (pp/pprint required))])
                response
                (into ["-------------------------"
                       "Response"
                       ""
                       (with-out-str (pp/pprint response))])
                refs
                (into ["-------------------------"
                       "Given"
                       ""
                       (with-out-str (pp/pprint refs))])))))

(defn doc
  "Given a client and an operation (keyword), prints documentation
  for that operation to the current value of *out*. Returns nil.

  Alpha. Subject to change."
  [client operation]
  (println (or (some-> client api/ops operation doc-str)
               (str "No docs for " (name operation)))))
