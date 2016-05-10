(ns morpheus.computation.codebase.base
  )

(def code-blocks (atom {}))

(defn new-code-block [id data]
  (swap! assoc code-blocks id data))

(defn get-code-block [id]
  (get @code-blocks id))

(defn obsolete-code-block [id]
  (swap! dissoc code-blocks id))