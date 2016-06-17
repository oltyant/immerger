(ns immerger.web.state
  (:require [immerger.web :as page]))

(def drop-path (ref ""))

(defn set-drop-path [path]
  (dosync
   (ref-set drop-path path)))
