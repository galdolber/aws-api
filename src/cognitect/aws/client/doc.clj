(ns cognitect.aws.client.doc
  (:require [clojure.string :as str]
            [cognitect.aws.dynaload :as dynaload]
            [cognitect.aws.client.api :as api]))

(def ^:private pprint-ref (delay (dynaload/load-var 'clojure.pprint/pprint)))
(defn ^:skip-wiki pprint
  "For internal use. Don't call directly."
  [& args]
  (binding [*print-namespace-maps* false]
    (apply @pprint-ref args)))

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
                       (with-out-str (pprint request))])
                required
                (into ["Required"
                       ""
                       (with-out-str (pprint required))])
                response
                (into ["-------------------------"
                       "Response"
                       ""
                       (with-out-str (pprint response))])
                refs
                (into ["-------------------------"
                       "Given"
                       ""
                       (with-out-str (pprint refs))])))))

(defn doc
  "Given a client and an operation (keyword), prints documentation
  for that operation to the current value of *out*. Returns nil.

  Alpha. Subject to change."
  [client operation]
  (println (or (some-> client api/ops operation doc-str)
               (str "No docs for " (name operation)))))
