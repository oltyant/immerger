(ns immerger.mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [clojure.string :as str]
            [immerger.executor.runner :as runner]
            [clojure.tools.logging :as log])
  (:import org.bson.types.ObjectId))

(def ^:const immerger-db-name "immerger")
(def ^:const immerger-coll-names
  {:metadata "metadata"
   :quests "quests"
   :measures "measures"
   :stats "stats"
   :languages "languages"
   :solutions "solutions"})

(defn mongodb-up? []
  (let [status (:out (runner/execute-command ["sh" "/etc/init.d/mongodb" "status"]))
        status-vec (str/split status #"\n")
        status-regex (partial re-find #"Active: \b(?:\w+)\b \((\w+)\)")
        status-result (some #(= (last (status-regex %1)) "running") status-vec)]
    (log/info (str "MongoDB up and running? : " status-result))
    status-result))

(defn get-mongo-config [dbname collname]
  (let [connection (mg/connect)
        db (mg/get-db connection dbname)]
    (log/info (str "Mongo connection config: " connection " db: " db))
    {:connection connection
     :db db
     :collection collname}))

(defn check-coll-empty? [config]
  (log/info (str "check collection emptiness: " config) )
  (mc/empty? (:db config) (:collection config)))

(defn get-mongo-config-coll [coll]
  (get-mongo-config immerger-db-name coll))

(defn insert [db collname documents]
  (let [cnt (count documents)
        oid (ObjectId.)
        bsons (map #(merge % oid) documents)]
    (log/info (apply str (conj ["insert entries into mongo: "] bsons)))
    (map #(mc/insert-and-return db collname %) bsons)))

(defn init-mongo []
  (log/info "checking mongodb status")
  (if (mongodb-up?)
    (for [[key val] (seq immerger-coll-names)]
      (try
        (let [mg-config (get-mongo-config-coll val)
              init-file-name (str "initdb/" val)
              init-file-content (slurp init-file-name)
              docs (read-string init-file-content)]
          (when (check-coll-empty? mg-config)
            (insert (:db mg-config) val docs)))
        (catch Exception e
          (str "Caught Exception: " (.getMessage e)))))
    ["cannot find a running mongodb server instance"]))
