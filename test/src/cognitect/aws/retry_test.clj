;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns cognitect.aws.retry-test
  (:require [clojure.test :refer [deftest is testing run-tests]]
            [cognitect.aws.retry :as retry]))

(defn default-retriable? [http-response]
  (contains? #{:cognitect.anomalies/busy
               :cognitect.anomalies/unavailable}
             (:cognitect.anomalies/category http-response)))

(deftest test-no-retry
  (is (= {:this :map}
         (retry/with-retry
           (constantly {:this :map})
           (constantly false)
           (constantly nil)))))

(deftest test-with-default-retry
  (testing "nil response from backoff"
    (is (= {:this :map}
           (retry/with-retry
             (constantly {:this :map})
             (constantly true)
             (constantly nil)))))
  (testing "always busy"
    (let [max-retries 2]
      (is (= {:cognitect.anomalies/category :cognitect.anomalies/busy :test/attempt-number 3}
             (let [pos (atom 0)
                   ret [{:cognitect.anomalies/category :cognitect.anomalies/busy
                         :test/attempt-number 1}
                        {:cognitect.anomalies/category :cognitect.anomalies/busy
                         :test/attempt-number 2}
                        {:cognitect.anomalies/category :cognitect.anomalies/busy
                         :test/attempt-number 3}]]
               (retry/with-retry
                 (fn []
                   (let [r (nth ret @pos)]
                     (swap! pos inc)
                     r))
                 default-retriable?
                 (retry/capped-exponential-backoff 50 500 max-retries)))))))
  (testing "always unavailable"
    (let [max-retries 2]
      (is (= {:cognitect.anomalies/category :cognitect.anomalies/unavailable :test/attempt-number 3}
             (let [pos (atom 0)
                   ret [{:cognitect.anomalies/category :cognitect.anomalies/unavailable
                         :test/attempt-number 1}
                        {:cognitect.anomalies/category :cognitect.anomalies/unavailable
                         :test/attempt-number 2}
                        {:cognitect.anomalies/category :cognitect.anomalies/unavailable
                         :test/attempt-number 3}]]
               (retry/with-retry
                 (fn []
                   (let [r (nth ret @pos)]
                     (swap! pos inc)
                     r))
                 default-retriable?
                 (retry/capped-exponential-backoff 50 500 max-retries)))))))
  (testing "3rd time is the charm"
    (let [max-retries 3]
      (is (= {:test/attempt-number 3}
             (let [pos (atom 0)
                   ret [{:cognitect.anomalies/category :cognitect.anomalies/busy
                         :test/attempt-number 1}
                        {:cognitect.anomalies/category :cognitect.anomalies/busy
                         :test/attempt-number 2}
                        {:test/attempt-number 3}]]
               (retry/with-retry
                 (fn []
                   (let [r (nth ret @pos)]
                     (swap! pos inc)
                     r))
                 default-retriable?
                 (retry/capped-exponential-backoff 50 500 max-retries))))))))

(comment
  (run-tests)
  )
