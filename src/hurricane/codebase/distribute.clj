(ns morpheus.computation.codebase.distribute
  (:require [cluster-connector.native-cache.core :refer [defcache assoc-items]]
            [neb.core :as neb]))

;; Distribute code or project with dependency to the cluster.
;; This design first appeared in: https://github.com/shisoft/-hurricane, a Hackathon project.

(defn gen-id neb/rand-cell-id)

(defn distribute-code [code-str]
  (let [id (gen-id)]
    (let [first-list (read-string code-str)
          contains-ns (= 'ns (first first-list))
          ns-sym (if contains-ns (second first-list) (str "" id))
          code-block (concat )]
      )))

(defn distribute-package [package-path]
  )


