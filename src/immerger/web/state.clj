(ns immerger.web.state
  (:require [immerger.executor.runner :as runner]))

(def drop-path (ref ""))

(defn set-drop-path [path]
  (dosync
   (ref-set drop-path path)))

(defn os-supported? [oss]
  (let [os (System/getProperty "os.name")]
    (some #(.equalsIgnoreCase % os) oss)))
