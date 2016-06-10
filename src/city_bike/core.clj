(ns city-bike.core
  (require [clojure.string :as str]
           [clojure.java.io :as io])
  (:gen-class))

(def header [:duration :start-time :stop-time :start-station-id
             :start-station-name :start-station-lat :start-station-long
             :end-station-id :end-station-name :end-station-lat
             :end-station-long :bike-id :user-type :birth :gender])

(defn get-csv-files
  "Looks into the current directory's files and filds all
  files that match are csv file. Puts them into a vector."
  []
  (let [file-vec (vec (file-seq (io/file (System/getProperty "user.dir"))))]
    (map #(.getName %) (filter #(boolean (re-find #".csv" (.getName %))) file-vec))))

(defn parse-csv-data
  "Reads all the data from csv file and splits the data into a map data structure.
  First splits based of a new-line then commas. Data is then put into a map with the header
  keyword being the map key."
  [file-name]
  (->> (slurp file-name)
       (str/split-lines)
       (map #(str/replace % #" " "-"))
       (map #(str/split % #","))
       (rest)
       (map #(zipmap header %))))

(println (map parse-csv-data (get-csv-files)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
