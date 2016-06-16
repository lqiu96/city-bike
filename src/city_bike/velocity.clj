(ns city-bike.velocity
  (require [city-bike.filter :as filter]
           [city-bike.helper :as helper]))

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
  (let [lat1-list (helper/get-data data :start-station-lat)
        long1-list (helper/get-data data :start-station-long)
        lat2-list (helper/get-data data :end-station-lat)
        long2-list (helper/get-data data :end-station-long)]
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
        time-values (helper/get-data data :duration)]
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
    (println (str "Unknown: " (average (calculate-average-velocity (filter/filter-unknown data))) " m/s"))
    (println (str "Male: " (average (calculate-average-velocity (filter/filter-male data))) " m/s"))
    (println (str "Female: " (average (calculate-average-velocity (filter/filter-female data))) " m/s"))))

(defn compare-sub-cust-velocity-average
  "Compares the average values of the genders and
  prints out the average velocity of subscribers vs customers"
  [data]
  (do
    (println (str "Customer: " (average (calculate-average-velocity (filter/filter-customers data))) " m/s"))
    (println (str "Subscriber: " (average (calculate-average-velocity (filter/filter-subscribers data))) " m/s"))))