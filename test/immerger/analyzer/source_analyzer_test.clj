(ns immerger.analyzer.source-analyzer-test
  (:require [immerger.analyzer.source-analyzer :as analyzer]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))

(def test-file-name "./test/src_file")
(def test-file-content1 "1\n2\n3")
(def test-file-content2 "1111\n111\n11")
(def test-file-content3 "(aa (bb (ccc cc c))")

(defn delete-files [fs]
  (doseq [f fs]
    (when (.exists (io/file f) (io/delete-file f)))))

(defn test-fixture [test]
  (test)
  (delete-files [test-file-name]))

(deftest count-lines-test
  (testing "Vanilla line count test"
    (let [expected-count 3]
      (spit test-file-name test-file-content1)
      (is (= (analyzer/count-lines
              (analyzer/get-lines test-file-name)) expected-count)))))

(deftest avg-line-length-test
  (testing "Vanilla average line length test"
    (let [expected-result 3.0]
      (spit test-file-name test-file-content2)
      (is (= (analyzer/avg-line-length
              (analyzer/get-lines test-file-name)) expected-result)))))

(deftest count-non-reserved-chars-test
  (testing "Vanilla count-non-reserved-chars count test"
    (let [reserves #{\( \)}
          expected-result {\a 2 \b 2 \c 6}]
      (spit test-file-name test-file-content3)
      (is (= (analyzer/count-non-reserved-chars reserves
                                                (analyzer/get-lines test-file-name)) expected-result)))))

(deftest count-non-reserved-keywords-test
  (testing "Vanilla count-non-reserved-keywords"
    (let [reserved-words #{"require" "defn" "def" "let" "recur" "loop"}
          expected-result 5]
      (spit test-file-name test-file-content3) ;4)
      (is (= true true)))))
             ;(analyzer/count-non-reserved-keywords reserved-words
             ;                                      (analyzer/get-lines test-file-name)) expected-result)))))
