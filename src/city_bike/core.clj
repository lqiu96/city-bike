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
       (rest)                                               ; Remove the header from the file
       (map #(zipmap header %))))                           ; Create a map with header as the keys

; Data filtering functions
(defn filter-data
  "Filters out the data based on the data type inputted"
  [data type filter-type]
  (filter #(= type (% filter-type)) data))

(defn filter-customers
  "Filters the data to get users are only customers"
  [data]
  (filter-data data "Customer" :user-type))

(defn filter-subscribers
  "Filters the data to get users are only subscribers"
  [data]
  (filter-data data "Subscriber" :user-type))

(defn filter-unknown
  "Filters the data to get users have unkown gender"
  [data]
  (filter-data data "0" :gender))

(defn filter-male
  "Filters the data to get users are male"
  [data]
  (filter-data data "1" :gender))

(defn filter-female
  "Filters the data to get users are female"
  [data]
  (filter-data data "2" :gender))

; Data Retreiving Function
(defn get-data
  "Retreives the type of data into a list"
  [data type]
  (map type data))

; Distance Calculating Functions
(defn haversine
  [lat1 long1 lat2 long2]
  (let [R 6372.8
        dlat (Math/toRadians (- lat2 lat1))
        dlong (Math/toRadians (- long2 long1))
        lat1 (Math/toRadians lat1)
        lat2 (Math/toRadians lat2)
        a (+ (Math/pow (Math/sin (/ dlat 2)) 2)
             (* (Math/cos lat1) (Math/cos lat2) (Math/pow (Math/sin (/ dlong 2)) 2)))]
    (* R 2 (Math/asin (Math/sqrt a)))))

(defn average-velocity
  [distance time]
  (/ (* distance 1000) time))

(defn velocity-data
  [data]
  (let [lat1-list (map read-string (get-data data :start-station-lat))
        long1-list (map read-string (get-data data :start-station-long))
        lat2-list (map read-string (get-data data :end-station-lat))
        long2-list (map read-string (get-data data :end-station-long))
        time (map read-string (get-data data :duration))]
    (map vector lat1-list long1-list lat2-list long2-list time)))

(defn calculate-average-velocity
  [vel-data]
  (let [time-values (map #(nth % 4) vel-data)]
    (->> vel-data
         (map #(haversine (first %) (second %) (nth % 2) (nth % 3)))
         (map #(average-velocity %2 %1) time-values))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
