(ns immerger.analyzer.source-analyzer
  (:require [clojure.java.io :as io]))

(defn line-count [f]
  (with-open [rdr (io/reader f)]
    (count (line-seq rdr))))

(defn average-line-length [f]
  (with-open [rdr (io/reader f)]
    (let [input-lines (line-seq rdr)
          line-count (count input-lines)
          char-count (reduce #(+ %1 (count %2)) 0 input-lines)]
      (float (/ char-count line-count)))))
