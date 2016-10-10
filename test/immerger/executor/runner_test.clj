(ns immerger.executor.runner-test
  (:require [immerger.executor.runner :as exec]
            [clojure.test :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def test-python-cmd ["ls" "-larth"])

(deftest command-line-runner-test
  (testing "Vanilla command line runner test"
    (let [result (exec/execute-command test-python-cmd)]
      (is (> (count (:out result)) 0)))))

(deftest command-line-runner-test2
  (testing "Vanilla command line runner with vargs"
    (let [result (apply exec/execute-command test-python-cmd)]
      (is (> (count (:out result)) 0)))))

