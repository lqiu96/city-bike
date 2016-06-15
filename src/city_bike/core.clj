(ns city-bike.core
  (require [clojure.string :as str]
           [clojure.java.io :as io])
  (:gen-class)
  (:import (clojure.lang Symbol)))

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

; Data filtering functions
(defn filter-data
  "Filters out the data based on the data type inputted.
  Allows for comparison based on comparator supplied"
  [data comparison type filter-type]
  (filter #(comparison (if (= Symbol (read-string (% filter-type)))
                         (% filter-type)
                         (read-string (% filter-type))) type) data))

; Functions which filter out certain people
(defn filter-duration
  "Filters the data to get users based on duration of bike ride"
  [data comparator duration]
  (filter-data data comparator duration :duration))

(defn filter-customers
  "Filters the data to get users are only customers"
  [data]
  (filter-data data = "Customer" :user-type))

(defn filter-subscribers
  "Filters the data to get users are only subscribers"
  [data]
  (filter-data data = "Subscriber" :user-type))

(defn filter-unknown
  "Filters the data to get users have unkown gender"
  [data]
  (filter-data data = 0 :gender))

(defn filter-male
  "Filters the data to get users are male"
  [data]
  (filter-data data = 1 :gender))

(defn filter-female
  "Filters the data to get users are female"
  [data]
  (filter-data data = 2 :gender))

(defn filter-bike-id
  "Filters the data to get users based on bike's id"
  [data comparator id]
  (filter-data data comparator id :bike-id))

; Data Retreiving Function
(defn get-data
  "Retreives the type of data into a list"
  [data type]
  (map type data))

; Distance and Velocity Calculating Functions
(defn haversine
  "Calculates the distance in kilometers between two locations
  given the two latitudes and longitudes. It is caluclated from the
  Haversine Function (https://rosettacode.org/wiki/Haversine_formula#clojure)"
  [lat1 long1 lat2 long2]
  (let [R 6372.8
        dlat (Math/toRadians (- lat2 lat1))                 ; Java Math requires angles in radians
        dlong (Math/toRadians (- long2 long1))
        lat1 (Math/toRadians lat1)
        lat2 (Math/toRadians lat2)
        a (+ (Math/pow (Math/sin (/ dlat 2)) 2)
             (* (Math/cos lat1) (Math/cos lat2) (Math/pow (Math/sin (/ dlong 2)) 2)))]
    (* R 2 (Math/asin (Math/sqrt a)))))

(defn velocity-data
  "Creates a list of vectors that holds a list of latitudes,
  longitudes, and trip durations"
  [data]
  (let [lat1-list (map read-string (get-data data :start-station-lat))
        long1-list (map read-string (get-data data :start-station-long))
        lat2-list (map read-string (get-data data :end-station-lat))
        long2-list (map read-string (get-data data :end-station-long))]
    (map vector lat1-list long1-list lat2-list long2-list))) ; Creates a list of vectors which nth values are grouped

(defn average-velocity
  "Finds the average velocity of the bike trip. Assumes that
  the distance is given in kilometers and the time is given in seconds"
  [distance time]
  (/ (* distance 1000) time))

(defn calculate-average-velocity
  "Calculates the average velocity of every single value
  stored the list of vectors"
  [data]
  (let [vel-data (velocity-data data)
        time-values (map read-string (get-data data :duration))]
    (->> vel-data
         (map #(haversine (first %) (second %) (nth % 2) (nth % 3))) ; Argument supplied to map is a vector [1 2 3] not 1 2 3
         (map #(average-velocity %2 %1) time-values))))              ; Thread-last macro required its value as first parameter

(defn average
  "Calculate the average based on the values of the numbers.
  Adds up all the numbers in a list and divides by the number in the list"
  [nums]
  (/ (apply + nums)
     (count nums)))

(defn compare-gender-velocity-average
  "Compares the average values of the genders and
  prints out the average velocity of all the genders"
  [data]
  (do
    (println (str "Unknown: " (average (calculate-average-velocity (filter-unknown data))) " m/s"))
    (println (str "Male: " (average (calculate-average-velocity (filter-male data))) " m/s"))
    (println (str "Female: " (average (calculate-average-velocity (filter-female data))) " m/s"))))

(defn compare-sub-cust-velocity-average
  "Compares the average values of the genders and
  prints out the average velocity of subscribers vs customers"
  [data]
  (do
    (println (str "Customer: " (average (calculate-average-velocity (filter-customers data))) " m/s"))
    (println (str "Subscriber: " (average (calculate-average-velocity (filter-subscribers data))) " m/s"))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
