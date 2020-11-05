;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns cognitect.aws.ec2-metadata-utils-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :as a]
            [org.httpkit.client :as http]
            [cognitect.aws.test.ec2-metadata-utils-server :as ec2-metadata-utils-server]
            [cognitect.aws.ec2-metadata-utils :as ec2-metadata-utils]))

(def ^:dynamic *test-server-port*)
(def ^:dynamic *http-client*)

(defn http-client
  ([req]
   (let [ch (a/chan 1)]
     (http-client req ch)
     ch))
  ([{:keys [uri scheme server-port server-name] :as req} ch]
   (http/request
    (-> req
        (assoc :url (str (name scheme) "://" server-name ":" server-port uri))
        (update :headers #(into {} (mapv (fn [[k v]] [(name k) v]) %))))
    (fn [resp] (a/put! ch resp)))))

(defn test-server
  [f]
  ;; NOTE: starting w/ 0 generates a random port
  (let [server-stop-fn   (ec2-metadata-utils-server/start 0)
        test-server-port (-> server-stop-fn meta :local-port)]
    (try
      (System/setProperty ec2-metadata-utils/ec2-metadata-service-override-system-property
                          (str "http://localhost:" test-server-port))
      (binding [*test-server-port* test-server-port
                *http-client*      http-client]
        (f))
      (finally
        (server-stop-fn)
        (System/clearProperty ec2-metadata-utils/ec2-metadata-service-override-system-property)))))

(use-fixtures :once test-server)

(deftest returns-nil-after-retries
  (is (nil? (ec2-metadata-utils/get-ec2-instance-region *http-client*))))

(comment
  (run-tests))
