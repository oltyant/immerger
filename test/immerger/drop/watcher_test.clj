(ns immerger.drop.watcher-test
  (:require [immerger.drop.watcher :as watcher]
            [immerger.web.state :as web]
            [clojure.java.io :as io]))
(use 'clojure.test)

(def file-name "./test/test_file")

(defn test-fixture [test]
        (test)
        (io/delete-file file-name))

(use-fixtures :once test-fixture)

(deftest drop-a-file
  (let [res (ref (java.io.File. "unwatched_file"))]
    (web/set-drop-path "./test")
    (watcher/subscribe #(ref-set res %))
    (watcher/start-watch)
    (spit file-name file-name)
    (loop [] (when (not  (.exists @res)) (do (java.lang.Thread/sleep 500) (recur))))
    (shutdown-agents)
    (is (= (slurp file-name) (slurp @res)))))
