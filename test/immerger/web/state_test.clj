(ns immerger.web.state-test
  (:require [immerger.web.state :as wstate]
            [clojure.test :refer :all]))

(deftest os-supported-test
  (testing "Should give back true if the list contains the os running on"
    (is (wstate/os-supported? ["Linux" "Windows"]))))

(deftest test-set-drop-path
  (testing "Should result the update of drop path"
    (let [newpath "new_path"]
      (wstate/set-drop-path newpath)
      (is (= @wstate/drop-path newpath)))))

