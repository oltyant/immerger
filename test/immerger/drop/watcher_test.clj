(ns immerger.drop.watcher-test
  (:require [immerger.drop.watcher :as watcher]
            [immerger.web.state :as web]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def file-name "./test/test_file")

(defn test-fixture [test]
        (test)
        (when (.exists (io/file file-name)) (io/delete-file file-name)))

(use-fixtures :once test-fixture)

(deftest drop-a-file
  (testing "Simple file drop"
    (web/set-drop-path "./test")
    (watcher/subscribe #((is (= (slurp file-name) (slurp %)))))
    (watcher/start-watch)
    (Thread/sleep 1000)
    (spit file-name file-name)
    (Thread/sleep 1000)))
