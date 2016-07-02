(ns immerger.drop.watcher
  (:require [hawk.core :as hawk]
            [immerger.web.state :as web]
            [clojure.java.io :as io]))

(def observers (atom #{}))

(defn subscribe [observer]
  (swap! observers conj observer))

(defn publish [file]
  (map #(apply % file) @observers)
  (io/delete-file file))

(defn start-watch []
;;TODO check whether running already one
  (future
    (hawk/watch! 
     [{:paths [@web/drop-path]
       :handler (fn [ctx e]
                  (when (and (:file e) (= :create (:kind e)))
                    (println (str "Watcher poll: " @web/drop-path " found " (:file e) " with change reason: " (:kind e)))
                    (publish (:file e))))}])))


