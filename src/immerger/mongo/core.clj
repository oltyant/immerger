(ns immerger.mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(defn get-mongo-config [dbname collname]
  (let [connection (mg/connect)
        db (mg/get-db connection dbname)]
    {:connection connection
     :db db
     :collection collname}))

(defn check-db-empty? [config]
  (mc/empty? (:db config) (:collection config)))

(def immerger-db-name "immerger")

(def immerger-coll-names {:metadata "metadata" :quests "quests" :measures "measures" :stats "stats" :languages "languages" :solutions "solutions"})

(defn get-mongo-config-coll [coll] (get-mongo-config immerger-db-name coll))

(defn create-db [config] ())

(defn init-mongo []
  (doseq [[key val] (seq immerger-coll-names)]
    (try
      (when (check-db-empty? (get-mongo-config-coll val)) (println "DB empty"))
      (catch Exception e (str "Caught Exception: " (.getMessage  e)))
      )))
