(ns immerger.web.handlers
  (:require [compojure.core :refer :all]
            [compojure.handler :as handlers]
            [hiccup.core :refer [html]]
            [hiccup.form :as f]
            [immerger.web.state :as web-status]
            [immerger.drop.watcher :as file-watcher]))

(defn index-handler [{session :session
                      params :params
                      response :response}]
  (let [{web-path :web-path} params
        valid? (fn [path] true)
        app-status (web-status/get-app-status)]
    (if (and web-path (valid? web-path))
      (do
        (web-status/set-drop-path web-path)
        (file-watcher/subscribe #(println %))
        (file-watcher/start-watch)
        (html [:p "web-path set to: " [:b web-path]]))
      (html [:p  (html [:spam app-status])
             (html [:spam (html (f/form-to [:post "/"]
                                           (f/label "Set Web Path" "Please give the drop path:")
                                           (f/text-field :web-path @web-status/drop-path)))])]))))

