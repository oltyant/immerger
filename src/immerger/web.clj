(ns immerger.web
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [hiccup.core :refer [html]]
            [hiccup.page :as page]
            [selmer.parser :refer [render-file]]
            [selmer.util :refer [without-escaping]]
            [immerger.web.handlers :as web-handlers]
            [clojure.string :as str]))

(defn index
  []
  (page/html5
   [:head
    [:title "The Immerger"]]
   [:body
    [:div {:id "content"} "The Immerger"]]))

(defn base
  ([request]
   (base request web-handlers/index-handler))
  ([request handler]
   (let [page-title "Immerger - Opening page"
         header-menus [{:title "Concept" :link ""}
                       {:title "About" :link ""}]
         title-link ""
         main-title (html [:a {:href ""} [:strong "Immerger "]
                           [:span "for the"] [:span " Truth "]
                           [:span "Seeker"]])
         subtitle "immerse languages and become a better programmer "
         menus [{:name "Immersing" :click "location.href='immersing'"}
                {:name "Quests" :click "location.href='quests'"}
                {:name "Languages" :click "location.href='languages'"}
                {:name "Measurements" :click "location.href='measures'"}
                {:name "Upload" :click "location.href='upload'"}]]
     (without-escaping
      (render-file "templates/index.html"
                   {:page-title page-title
                    :header-menus header-menus
                    :title-link title-link
                    :main-title main-title
                    :menus menus
                    :subtitle subtitle
                    :body-content (handler request)})))))

(defroutes routes
  (GET "/" request (base request))
  (POST "/set-drop-path" request (base request))
  (GET "/languages" request (base request web-handlers/languages-handler))
  (GET "/immersing" request (base request web-handlers/immerse-handler))
  (GET "/quests" request (base request web-handlers/quests-handler))
  (GET "/measures" request (base request web-handlers/measures-handler))
  (GET "/upload" request (base request web-handlers/upload-handler))
  (route/resources "/")
  (route/not-found "Page Not Found"))

(defn -main
  []
  (ring/run-jetty #'routes {:port 8080 :join? false}))
