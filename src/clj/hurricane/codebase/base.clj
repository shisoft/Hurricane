(ns hurricane.codebase.base
  (:require [morpheus.computation.codebase.package :refer [compile-path]])
  (:import (org.shisoft.hurricane UnzipUtil)
           (java.io File)))

(def code-blocks (atom {}))

(def codebase-def-ns "hurricane.code-block.")

(defn package-path [id]
  (str "packages/" (str id)))

(defn new-code-block* [id data]
  (swap! assoc code-blocks id data))

(defn get-code-block [id]
  (get @code-blocks id))

(defn obsolete-code-block [id]
  (let [orig-data (get-code-block id)]
    (swap! dissoc code-blocks id)
    (when (:pack? orig-data)
      (.delete (File. ^String (package-path id))))))

(defn new-code-block [id & {:keys [ns pack? code] :as data}]
  (when pack? (compile-path (package-path id)))
  (new-code-block* id (assoc data :code (when code (eval code)))))

(defn new-code-package [id data]
  (let [pack-path (package-path id)]
    (UnzipUtil/unzip data pack-path)
    (new-code-block id :pack? true)))