(ns hurricane.codebase.distribute
  (:require [cluster-connector.native-cache.core :refer [defcache assoc-items]]
            [hurricane.codebase.base :refer :all]
            [hurricane.codebase.zip-utils :refer :all]
            [neb.core :as neb]
            [cluster-connector.remote-function-invocation.core :as rfi])
  (:import (java.io File)))

;; Distribute code or project with dependency to the cluster.
;; This design first appeared in: https://github.com/shisoft/-hurricane, a Hackathon project.

(defn gen-id neb/rand-cell-id)

(defn distribute-code [code-str]
  (let [id (gen-id)
        first-list (read-string code-str)
        contains-ns (= 'ns (first first-list))
        ns-str (if contains-ns (second first-list) (str codebase-def-ns id))
        ns-sym (symbol ns-str)
        code-block (read-string (str "(do "
                                     (when-not contains-ns "(ns " ns-str ")")
                                     code-str ")"))]
    (rfi/broadcast-invoke 'hurricane.codebase.base/new-code-block id {:ns ns-sym :code code-block})))

(defn distribute-package [package-path]
  (assert (.exists (File. (str package-path "/project.clj")))
          "Couldn't find project.clj, which is need for tracking dependices.")
  (let [id (gen-id)
        zip-out-stream (zip-in-mem package-path)
        zip-data (.toByteArray zip-out-stream)]
    (rfi/broadcast-invoke 'hurricane.codebase.base/new-code-package id zip-data)))