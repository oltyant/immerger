(ns immerger.web.state)

(def drop-path (ref (str (or
                          (System/getenv "TEMP")
                          (System/getenv "TMP")
                          (System/getenv "HOME"))
                         (System/getProperty "file.separator")
                         "immerger_drop")))

(defn set-drop-path [path]
  (dosync
   (ref-set drop-path path)))
