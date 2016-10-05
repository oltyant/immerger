(ns immerger.executor.runner
  (:require [clojure.java.shell :as shell]))

(defn execute-command
  ([cmd & args]
   (let [command (vec (cons cmd args))]
     (apply shell/sh command)))
  ([coll]
   (apply shell/sh coll)))
