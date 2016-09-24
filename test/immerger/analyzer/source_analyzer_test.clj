(ns immerger.analyzer.source-analyzer-test
  (:require [immerger.analyzer.source-analyzer :as analyzer]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(def test-file-name "./test/src_file")

(defn test-fixture [test]
  (test)
  (when (.exists (io/file)) (io/delete-file test-file-name)))

(deftest line-count-test
  (testing "Vanilla line count test"
    (let [content "1\n2\n3"
          expected-count 3]
      (spit test-file-name content)
      (is (= (analyzer/line-count test-file-name) expected-count)))))

(deftest average-line-length-test
  (testing "Vanilla average line length"
    (let [content "1111\n11\n111\n1"
          expected-result 2.5]
      (spit test-file-name content)
      (is (= (analyzer/average-line-length test-file-name) expected-result)))))
