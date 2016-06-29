(defproject hurricane "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.cemerick/pomegranate "0.3.1"]
                 [leiningen-core "2.6.1"]
                 [cluster-connector "0.1.0-SNAPSHOT"]
                 [com.climate/claypoole "1.1.2"]
                 [net.openhft/koloboke-api-jdk8 "0.6.8"]
                 [net.openhft/koloboke-impl-jdk8 "0.6.8"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"])
