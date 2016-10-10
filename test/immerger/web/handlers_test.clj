(ns immerger.web.handlers-test
  (:require [immerger.web.handlers :refer :all]
            [hiccup.core :refer [html]]
            [hiccup.form :as f]
            [clojure.test :refer :all]))

(def ^:const web-path "path")
(def ^:const test-request {:session ""
                           :params {:web-path web-path}
                           :response ""})
(def ^:const test-request-result
  (html [:p "web-path set to: " [:b web-path]]))

(deftest index-handler-test
  (testing "Should give back the proper html tags"
    (let [html-res (index-handler test-request)
          regex (str "web-path set to:\\s+<b>" web-path "\\s*</b>")
          match (re-find (re-pattern regex) html-res)]
      (is match))))
