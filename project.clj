(defproject city-bike "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/lqiu96/city-bike"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot city-bike.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
