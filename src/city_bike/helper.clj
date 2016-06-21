(ns city-bike.helper
  (:import (clojure.lang Symbol)))

(defn convert-to-object
  "Reads the value of the String and attempts to convert into its type.
  The string becomes a number if it can be reader. Otherwise it stays a String"
  ([value]
   (if (= Symbol (type (read-string value)))
     value
     (read-string value))))

(defn convert-to-function
  "Attempts to read the String and convert to a function.
  If it is already a function type, it does nothing"
  [s]
  (if (= String (type s))
    (resolve (read-string s))
    s))

; Data Retreiving Function
(defn get-data
  "Retreives the type of data into a list"
  [data type]
  (->> data
       (map type)
       (map convert-to-object)))