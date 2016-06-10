(ns city-bike.core
  (require [clojure.string :as str])
  (:gen-class))

(def header [:duration :start-time :stop-time :start-station-id
             :start-station-name :start-station-lat :start-station-long
             :end-station-id :end-station-name :end-station-lat
             :end-station-long :bike-id :user-type :birth :gender])

(defn parse-csv-data
  [file-name]
  (->> (slurp file-name)
       (str/split-lines)
       (map #(str/replace % #" " "-"))
       (map #(str/split % #","))
       (rest)
       (map #(zipmap header %))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
