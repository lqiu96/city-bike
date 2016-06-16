(ns city-bike.core
  (require [city-bike.data :as data]
           [city-bike.velocity :as vel])
  (:gen-class))

(def data (data/parse-csv-data "Test.csv"))
(vel/compare-gender-velocity-average data)
(vel/compare-sub-cust-velocity-average data)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
