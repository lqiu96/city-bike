(ns city-bike.core
  (require [clojure.string :as str]
           [clojure.java.io :as io])
  (:gen-class))

; These are the headers in the CSV file
(def header [:duration :start-time :stop-time :start-station-id
             :start-station-name :start-station-lat :start-station-long
             :end-station-id :end-station-name :end-station-lat
             :end-station-long :bike-id :user-type :birth :gender])

(defn get-csv-files
  "Looks into the current directory's files and filds all
  files that match are csv file. Puts them into a vector."
  []
  (let [file-vec (-> (System/getProperty "user.dir")        ; Get all the files in the current directory
                     (io/file)                              ; Cast to Java File type
                     (file-seq)                             ; File seq of directory
                     (vec))]                                ; Convert to vector
    (->> file-vec
         (filter #(boolean (re-find #".csv" (.getName %)))) ; Filter out any file names without .csv extension
         (map #(.getName %)))))                             ; Get the names of the filtered vector

(defn parse-csv-data
  "Reads all the data from csv file and splits the data into a map data structure.
  First splits based of a new-line then commas. Data is then put into a map with the header
  keyword being the map key."
  [file-name]
  (->> (slurp file-name)                                    ; Read the data straight from file
       (str/split-lines)                                    ; Split based on new line character
       (map #(str/replace % #" " "-"))                      ; Replace spaces with dash
       (map #(str/replace % #"\"" ""))                      ; Each line has quotes (\") around data, removes it
       (map #(str/split % #","))                            ; Split based on CSV deliminter: comma (,)
       (rest)                                               ; Remove the header line from the file
       (map #(zipmap header %))))                           ; Create a map with header as the keys

; Data Retreiving Function
(defn get-data
  "Retreives the type of data into a list"
  [data type]
  (map type data))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
