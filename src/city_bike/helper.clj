(ns city-bike.helper
  (:import (clojure.lang Symbol)))

(defn convert-to-object
  ([value]
   (if (= Symbol (type (read-string value)))
     value
     (read-string value)))
  ([data filter-type]
   (if (= Symbol (type (read-string (data filter-type))))
     (data filter-type)
     (read-string (data filter-type)))))

(defn convert-to-function
  [s]
  (resolve (read-string s)))

; Data Retreiving Function
(defn get-data
  "Retreives the type of data into a list"
  [data type]
  (->> data
       (map type)
       (map convert-to-object)))