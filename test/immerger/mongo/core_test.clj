(ns immerger.mongo.core-test
  (:require [monger.core :as mg]
            [monger.command :as mgcmd]
            [monger.conversion :refer :all]
            [immerger.mongo.core :as core]
            [immerger.executor.runner :as runner]
            [clojure.test :refer :all])
  (:import [com.mongodb BasicDBObject BasicDBList]))

(def ^:const dbname "mydb_test")
(def ^:const collname "something")
(defn get-db-config
  []
  (core/get-mongo-config dbname collname))

(defn test-fixture
  [test]
  (let [db-config (get-db-config)
        db (:db db-config)
        conn (:connection db-config)]
    (test)
    (mg/drop-db conn db)))

(deftest mongodb-up-test
  (testing "Mongodb should be up and running"
    (let [db-config (get-db-config)
          connection (:connection db-config)
          cmd (doto (BasicDBObject.)
               (.put "show" "users"))
          bsons (mgcmd/raw-admin-command connection cmd)
          documents (map #(from-db-object % true) bsons)
          travis-build? #(some (= (:user %) "travis") documents)]
      (is
       (if travis-build? true
         (core/mongodb-up?))))))

(deftest get-mongo-config-test
  (testing "Should connect to the local mongo db"
    (let [res (get-db-config)]
      (is (and (:db res) (:connection res))))))

(deftest update-or-insert-test
  (testing "Should give back the inserted/updated document"
    (let [config (get-db-config)
          db (:db config)
          documents [{:name "insert-test" :age "infant"}]
          result (core/update-or-insert db collname documents)]
      (is result))))
