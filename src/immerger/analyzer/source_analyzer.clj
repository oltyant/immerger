(ns immerger.analyzer.source-analyzer
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn get-lines [f]
  (str/split (slurp f) #"\n"))

(defn count-lines [lines]
  (count lines))

(defn avg-line-length [lines]
  (let [line-count (count lines)
        char-count (reduce #(+ %1 (count (str/trim %2))) 0 lines)]
    (float (/ char-count line-count))))

(defn count-non-reserved-chars [reserves lines]
  (let [clean-lines (map #(str/replace % #"\s" "") lines)
        content (seq (apply str clean-lines))
        non-reserves (filter #(not (contains? reserves %)) content)]
    (frequencies non-reserves)))
