(ns immerger.mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clojure.string :as str]
            [immerger.drop.watcher :as watcher] 
            [immerger.executor.runner :as runner])
  (:import org.bson.types.ObjectId))

(def mongodb-up?
  (let [status (:out (runner/execute-command ["sh" "/etc/init.d/mongodb" "status"]))
        status-vec (str/split status #"\n")
        status-regex (partial re-find #"Active: \b(\w+)\b \(\w+\)")
        status-result (reduce #(if-let [res (status-regex %2)] res %1) (vector) status-vec)]
    (case status-result
      [match status] (.equalsIgnoreCase status "running")
      nil)))

(defn get-mongo-config [dbname collname]
  (let [connection (mg/connect)
        db (mg/get-db connection dbname)]
    {:connection connection
     :db db
     :collection collname}))

(defn check-db-empty? [config]
  (mc/empty? (:db config) (:collection config)))

(def immerger-db-name "immerger")

(def immerger-coll-names
  {:metadata "metadata"
   :quests "quests"
   :measures "measures"
   :stats "stats"
   :languages "languages"
   :solutions "solutions"})

(defn get-mongo-config-coll [coll]
  (get-mongo-config immerger-db-name coll))

(defn create-db [config] ())

(defn insert [db collname documents]
  (let [cnt (count documents)
        oid (ObjectId.)
        bsons (map #(merge % oid) documents)]
    (map #(mc/insert-and-return db collname %) bsons)))

(defn init-mongo []
  (doseq [[key val] (seq immerger-coll-names)]
    (try
      (when (check-db-empty? (get-mongo-config-coll val))
        " ")
      (catch Exception e
        (str "Caught Exception: " (.getMessage e))))))
