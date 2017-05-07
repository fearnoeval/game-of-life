(defproject game-of-life "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins      [[lein-cljsbuild            "1.1.1"]]
  :dependencies [[org.clojure/clojure       "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/core.async    "0.2.374"]]
  :cljsbuild
    {:builds [{
      :source-paths ["src/cljs"]
      :compiler
        {:output-to "resources/main.js"
         :optimizations :advanced}}]})
