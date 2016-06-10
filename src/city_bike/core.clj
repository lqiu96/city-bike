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
  (let [file-vec (-> (System/getProperty "user.dir")        ;Get all the files in the current directory
                     (io/file)                              ;Cast to Java File type
                     (file-seq)                             ;File seq of directory
                     (vec))]                                ;Convert to vector
    (->> file-vec
         (filter #(boolean (re-find #".csv" (.getName %)))) ;Filter out any file names without .csv extension
         (map #(.getName %)))))                             ;Get the names of the filtered vector

(defn parse-csv-data
  "Reads all the data from csv file and splits the data into a map data structure.
  First splits based of a new-line then commas. Data is then put into a map with the header
  keyword being the map key."
  [file-name]
  (->> (slurp file-name)
       (str/split-lines)
       (map #(str/replace % #" " "-"))
       (map #(str/replace % #"\"" ""))
       (map #(str/split % #","))
       (rest)
       (map #(zipmap header %))))

(defn get-user-type
  [data type]
  (filter #(= type (% :user-type)) data))

(defn get-customers
  [data]
  (get-user-type data "Customer"))

(defn get-subscribers
  [data]
  (get-user-type data "Subscriber"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
