(ns cognitect.aws.api-test
  (:require [clojure.test :as t :refer [deftest is]]
            [cognitect.aws.http.cognitect :as http-client]
            [cognitect.aws.http :as http]))

(deftest test-stop
  (let [call-count (atom 0)
        shared-client (http-client/create)]
    (with-redefs [http/stop (fn [_] (swap! call-count inc))]
      (http/stop shared-client)
      (is (= 1 @call-count)))))
