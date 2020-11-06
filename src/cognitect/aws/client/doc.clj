(ns cognitect.aws.client.doc
  (:require [clojure.string :as str]
            [cognitect.aws.dynaload :as dynaload]
            [cognitect.aws.service :refer [ns-prefix service-name base-resource-path]]
            [cognitect.aws.client :as client]
            [clojure.walk :as walk]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))

(def ^:private pprint-ref (delay (dynaload/load-var 'clojure.pprint/pprint)))
(defn ^:skip-wiki pprint
  "For internal use. Don't call directly."
  [& args]
  (binding [*print-namespace-maps* false]
    (apply @pprint-ref args)))

(defonce svc-docs (atom {}))

(defn spec-ns
  "The namespace for specs for service."
  [service]
  (symbol (format "%s.specs" (ns-prefix service))))

(defn load-specs [service]
  (require (spec-ns service)))

(defn with-ref-meta [m op doc]
  (let [ref-atom    (atom nil)
        refs        (:refs doc)
        updated-doc (walk/postwalk
                     (fn [n]
                       (if  (contains? refs n)
                         (with-meta n
                           {'clojure.core.protocols/datafy #(-> ref-atom deref %)})
                         n))
                     doc)]
    (reset! ref-atom (:refs updated-doc))
    (assoc m op (into {:name (name op)} updated-doc))))

(defn docs
  "Returns the docs for this service"
  [service]
  (let [k (service-name service)]
    (if-let [doc (get @svc-docs k)]
      doc
      (-> (swap! svc-docs
                 assoc
                 k
                 (reduce-kv with-ref-meta
                            {}
                            (clojure.edn/read-string
                             (slurp
                              (io/resource (format "%s/%s/docs.edn" base-resource-path (service-name service)))))))
          (get k)))))

(defn request-spec-key
  "Returns the key to look up in the spec registry for the spec for
  the request body of op."
  [service op]
  (load-specs service)
  (when-let [shape-key (some->> service :operations op :input :shape)]
    (keyword (ns-prefix service) shape-key)))

(defn response-spec-key
  "Returns the key to look up in the spec registry for the spec for
  the response body of op."
  [service op]
  (load-specs service)
  (when-let [shape-key (some->> service :operations op :output :shape)]
    (keyword (ns-prefix service) shape-key)))

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

(defn ops
  "Returns a map of operation name to operation data for this client.

  Alpha. Subject to change."
  [client]
  (->> client
       client/-get-info
       :service
       docs))

(defn doc
  "Given a client and an operation (keyword), prints documentation
  for that operation to the current value of *out*. Returns nil.

  Alpha. Subject to change."
  [client operation]
  (println (or (some-> client ops operation doc-str)
               (str "No docs for " (name operation)))))
