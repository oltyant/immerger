(ns immerger.mongo.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer :all]
            [immerger.executor.runner :as runner]
            [clojure.string :as str]
            [clojure.java.io :as io]
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

(defn mongodb-up?
  []
  (let [status (:out
                (runner/execute-command
                 ["sh" "/etc/init.d/mongodb" "status"]))
        status-vec (str/split status #"\n")
        status-regex (partial re-find #"Active: \b(?:\w+)\b \((\w+)\)")
        status-result (some #(= (last (status-regex %1)) "running")
                            status-vec)]
    (log/info (str "Is MongoDB up and running? : " status-result))
    status-result))

(defn get-mongo-config
  [dbname collname]
  (let [connection (mg/connect)
        db (mg/get-db connection dbname)]
    (log/info (str "Mongo connection config: "
                   connection " db: " db))
    {:connection connection
     :db db
     :collection collname}))

(defn get-mongo-config-coll
  [coll]
  (get-mongo-config immerger-db-name coll))

(defn update-or-insert
  [db collname documents]
  (let [cnt (count documents)]
    (for [document documents]
      (do (log/info (str "update or insert document " document))
          (mc/save-and-return db collname document)))))

(defn init-mongo
  []
  (log/info "checking mongodb status")
  (if (mongodb-up?)
    (let [mg-config (get-mongo-config-coll val)]
      (for [[key val] immerger-coll-names]
        (try
          (log/info (str "Update or insert into Mongo collection: "
                         key " " val))
          (let [init-file-name (io/file (io/resource
                                  (str "initdb/" val ".map")))
                init-file-content (slurp init-file-name)
                docs (read-string init-file-content)]
            (log/info (str "file name: " init-file-name))
            (log/info (str "file content: "  init-file-content))
            (log/info docs)
            (update-or-insert (:db mg-config) val docs))
          (catch Exception e
            (str "Caught Exception: " (.getMessage e))))))
    ["cannot find a running mongodb server instance"]))
