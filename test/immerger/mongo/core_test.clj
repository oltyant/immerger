(ns immerger.mongo.core-test
  (:require [immerger.mongo.core :as core]
            [immerger.executor.runner :as runner]
            [clojure.test :refer :all]))

(deftest get-mongo-config-test
  (testing "Should connect to the local mongo db"
    (let [dbname "mydb_test"
          collname "something"
          res (core/get-mongo-config dbname collname)]
      (is (and (:db res) (:connection res))))))

(deftest check-db-empty-test
  (testing "After insertion it should give back false"
    (let [config (core/get-mongo-config "mydb_test" "something")
          newentry (core/insert (:db config)
                                "something" [{:name "John" :age 30 :pets ["Sam" "Chelsie"]}])]
        true)))
