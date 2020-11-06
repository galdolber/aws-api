;; Copyright (c) Cognitect, Inc.
;; All rights reserved.

(ns cognitect.aws.endpoint-test
  (:require [clojure.test :refer :all]
            [cognitect.aws.endpoint :as endpoint]))

(deftest test-resolve-endpoints
  (testing "resolves regionalized endpoints"
    (is (= "ec2.us-east-1.amazonaws.com" (:hostname (endpoint/resolve :ec2 :us-east-1)))))
  (testing "resolves global endpoints"
    (is (= "iam.amazonaws.com"
           (:hostname (endpoint/resolve :iam :us-east-1)))))
  (testing "uses defaults to resolve unspecified endpoints"

    (is (= "i-do-not-exist.us-east-1.amazonaws.com"
           (:hostname (endpoint/resolve :i-do-not-exist :us-east-1))))))


(comment
  (run-tests)

  )
