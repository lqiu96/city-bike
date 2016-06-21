(ns city-bike.filter
  (require [city-bike.helper :as helper]))

; Data filtering functions
(defn filter-data
  "Filters out the data based on the data type inputted.
  Allows for comparison based on comparator supplied"
  [data comparison type filter-type]
  (filter #((helper/convert-to-function comparison)         ; Convert the String into a function
            (helper/convert-to-object (% filter-type)) type) data)) ; Convert the String into Number

; Functions which filter out certain people
(defn filter-duration
  "Filters the data to get users based on duration of bike ride"
  [data comparator duration]
  (filter-data data comparator duration :duration))

(defn filter-start-station
  "Filters the data to get users based on start station id"
  [data comparator id]
  (filter-data data comparator id :start-station-id))

(defn filter-end-station
  "Filters the data to get users based on end station id"
  [data comparator id]
  (filter-data data comparator id :end-station-id))

(defn filter-bike-id
  "Filters the data to get users based on bike id"
  [data comparator id]
  (filter-data data comparator id :bike-id))

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