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

(deftest coll-valid-test
  (testing "Should give back valid for stats string"
    (is (core/coll-valid? "stats"))))

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

(deftest query-document-test
  (testing "Should give back the document inserted based on a proper criterion"
    (let [collname "stats"
          config (core/get-mongo-config dbname collname)
          db (:db config)
          documents [{:name "insert-test" :age "infant"}]
          expected (core/update-or-insert db collname documents)
          actual (first (core/query-document db collname {:age "infant"}))]
      (is (= (:age actual) (:age expected))))))

(deftest query-all-documents-test
  (testing "Should give back all the document in a collection"
    (let [collname "stats"
          config (core/get-mongo-config dbname collname)
          db (:db config)
          documents [{:name "insert-test" :age "infant"}]
          expected (first (core/update-or-insert db collname documents))
          actual (first (core/query-all-documents db collname))]
      (is (= (:age actual) (:age expected))))))
