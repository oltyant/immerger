(ns immerger.web.state
  (:require [immerger.executor.runner :as runner]
            [immerger.mongo.core :as mg]
            [clojure.tools.logging :as log]))

(def ^:const supported-oss ["Linux"])
(def drop-path (ref "drop_path"))
(def initialized (atom false))

(defn os-supported?
  [oss]
  (let [os (System/getProperty "os.name")]
    (some #(.equalsIgnoreCase % os) oss)))

(defn get-app-status
  []
  (if (not @initialized)
    (do (log/info "The system need to be initialized")
        (let [os-valid? (os-supported? supported-oss)
              mg-init (mg/init-mongo)]
          (swap! initialized #(not %))
          {:initialized @initialized
           :os-supported os-valid?
           :db-init mg-init}))
    (do (log/info "The system is initialized")
        {:initialized @initialized})))

(defn set-drop-path
  [path]
  (dosync
   (ref-set drop-path path)))
