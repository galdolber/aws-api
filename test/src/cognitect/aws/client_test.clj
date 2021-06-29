(ns cognitect.aws.client-test
  (:require [clojure.test :as t :refer [deftest testing is]]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [cognitect.aws.client.api :as aws]
            [cognitect.aws.client :as client]
            [cognitect.aws.client.validate :as validate]
            [cognitect.aws.region :as region]
            [cognitect.aws.credentials :as creds]))

(defn stub-http-client [result]
  (fn [_] result))

(defn stub-credentials-provider [creds]
  (reify creds/CredentialsProvider
    (fetch [_] creds)))

(defn stub-region-provider [region]
  (reify region/RegionProvider
    (fetch [_] region)))

(def params {:api :s3
             :service (json/parse-string (slurp (io/file "dev-resources/s3-2006-03-01.normal.json")) true)
             ;; use an anomaly to bypass parsing http-response
             :http-client          (stub-http-client {:cognitect.anomalies/category :cognitect.aws/test
                                                      :cognitect.anomalies/message  "test"})
             :region-provider      (stub-region-provider :us-east-1)
             :credentials-provider (stub-credentials-provider
                                    {:access-key-id     "a"
                                     :secret-access-key "b"})})

(deftest test-handle-http-response
  (testing "returns http-response if it is an anomaly"
    (is (= {:cognitect.anomalies/category :does-not-matter}
           (#'client/handle-http-response {} {} {:cognitect.anomalies/category :does-not-matter})))))

(deftest test-meta
  (let [res (aws/invoke (aws/client params) {:op :ListBuckets})]
    (testing "request meta includes :http-request"
      (is (=  {:uri "/"
               :server-name "s3.amazonaws.com"
               :body nil}
              (select-keys (:http-request (meta res)) [:uri :server-name :body]))))
    (testing "request meta includes raw response"
      (is (= {:cognitect.anomalies/category :cognitect.aws/test,
              :cognitect.anomalies/message "test",
              :body nil}
             (:http-response (meta res)))))))

(deftest test-providers
  (testing "base case"
    (let [aws-client (aws/client params)]
      (is (= "test"
             (:cognitect.anomalies/message
              (aws/invoke aws-client {:op :ListBuckets}))))))
  (testing "nil creds (regression test - should not hang)"
    (let [aws-client (aws/client (assoc params
                                        :credentials-provider
                                        (stub-credentials-provider nil)))]
      (println ">>>" (pr-str (aws/invoke aws-client {:op :ListBuckets})))
      (is (re-find #"^Unable to fetch credentials"
                   (:cognitect.anomalies/message
                    (aws/invoke aws-client {:op :ListBuckets}))))))
  (testing "empty creds (regression test - should not hang)"
    (let [aws-client (aws/client (assoc params
                                        :credentials-provider
                                        (stub-credentials-provider {})))]
      (is (= "test"
             (:cognitect.anomalies/message
              (aws/invoke aws-client {:op :ListBuckets}))))))
  (testing "nil region (regression test - should not hang)"
    (let [aws-client (aws/client (assoc params
                                        :region-provider
                                        (stub-region-provider nil)))]
      (is (re-find #"^Unable to fetch region"
                   (:cognitect.anomalies/message
                    (aws/invoke aws-client {:op :ListBuckets}))))))
  (testing "empty region (regression test - should not hang)"
    (let [aws-client (aws/client (assoc params
                                        :region-provider
                                        (stub-region-provider "")))]
      (is (re-find #"^No known endpoint."
                   (:cognitect.anomalies/message
                    (aws/invoke aws-client {:op :ListBuckets})))))))

(deftest validate-requests?
  (let [aws-client (aws/client params)]
    (is (not (validate/validate-requests? aws-client)))
    (validate/validate-requests aws-client true)
    (is (validate/validate-requests? aws-client))
    (validate/validate-requests aws-client false)
    (is (not (validate/validate-requests? aws-client)))))
