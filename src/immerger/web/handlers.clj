(ns immerger.web.handlers
  (:require [compojure.core :refer :all]
            [compojure.handler :as handlers]
            [hiccup.core :refer [html]]
            [hiccup.form :as f]
            [immerger.web.state :as web-status]
            [immerger.drop.watcher :as file-watcher]))

(defn format-data
  [maps-data]
  (let [wrap #(str (html [:div %]))
        convert-to-html (fn [m]
                          (reduce-kv
                           #(str %1
                                 (when (not= %2 :_id)
                                   (html [:p %2 (html [:spam %3])]))) "" m)) 
        content (for [m maps-data] (convert-to-html m))]
    (wrap content)))

(defn index-handler
  [{session :session
    params :params
    response :response}]
  (let [{web-path :web-path} params
        app-status (web-status/get-app-status)]
    (html [:h1 "Welcome in the Immerger!"])))

(defn default-handler [coll-name]
  (let [app-status (web-status/get-app-status)
        page coll-name
        data (web-status/get-data page)]
    (format-data data)))

(defn languages-handler
  [{session :session
    params :params
    response :response}]
  (default-handler "languages"))

(defn immerse-handler
  [{session :session
    params :params
    response :response}]
  (default-handler "solutions"))

(defn quests-handler
  [{session :session
    params :params
    response :response}]
  (default-handler "quests"))

(defn measures-handler
  [{session :session
    params :params
    response :response}]
  (default-handler "measures"))

(defn upload-handler
  [{session :session
    params :params
    response :response}]
  (default-handler "stats"))

(defn drop-path-handler
  [{session :session
    params :params
    response :response}]
  (let [{web-path :web-path} params
        app-status (web-status/get-app-status)]
    (if web-path
      (do
        (web-status/set-drop-path web-path)
        (file-watcher/subscribe #(println %))
        (file-watcher/start-watch)
        (html [:p "web-path set to: " [:b web-path]]))
      (html [:p  (html [:spam app-status])
             (html [:spam (html (f/form-to
                                 [:post "/"]
                                 (f/label "Set Web Path" "Please give the drop path:")
                                 (f/text-field :web-path @web-status/drop-path)))])]))))
