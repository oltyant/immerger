(defproject immerger "0.1.0-SNAPSHOT"
  :description "Web application for solving different problems space on different programming languages to obtain statistics and be able to compare them in their purest form"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.novemberain/monger "3.0.2"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.2"]
                 [hiccup "1.0.5"]
                 [selmer "1.0.4"]
                 [hawk "0.2.10"]])
