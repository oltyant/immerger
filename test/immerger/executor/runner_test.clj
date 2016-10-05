(ns immerger.executor.runner-test
  (:require [immerger.executor.runner :as exec]
            [clojure.test :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def test-python-cmd ["time" "python" "-c" "'print [(x,y) for x in xrange(100) for y in xrange(100) if x != y]'"])

(deftest command-line-runner-test
  (testing "Vanilla command line runner test"
    (let [result (exec/execute-command test-python-cmd)]
      (is (> (count (:err result)) 0)))))
