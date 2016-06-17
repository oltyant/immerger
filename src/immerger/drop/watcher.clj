(ns immerger.drop.watcher
  (:require [hawk.core :as hawk]
            [immerger.web.state :as web]
            ;;[immerger.log :as log]
            ))

(def observers (atom #{}))

(defn subscribe [observer]
  (swap! observers conj observer)
  (println observer " is subscribed!"))

(defn publish [file]
  (map #(apply % file) @observers)
  (println "file: " file " is observed!"))

(def watcher (agent ""))

(defn start-watch [] (send-off watcher
                               #(hawk/watch! %)
                               [{:paths [@web/drop-path]
                                 :handler (fn [ctx e]
                                            (when (:file e)
                                              (println (str "Watcher poll: " @web/drop-path " found " (:file e) " with change reason: " (:kind e)))
                                              ;;(log/debug (str "Watcher poll: " web/@drop-path " found " (:file e) " with change reason: " (:kind e)))
                                              
                                              (publish (:file e)))
                                            )}]))


