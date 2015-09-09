(ns upgradingdave.boot-cider
  (:require [boot.core :refer [deftask set-env! get-env]]))

(defn- find-cider-el 
  "Find cider.el file"
  []
  (let [h  (System/getProperty "user.home")
        e  (str h "/.emacs.d")
        d  (file-seq (java.io.File. e))
        fs (filter #(= "cider.el" (.getName %)) d)]
    (cond

      (empty? fs)
      nil

      (> 1 (count f))
      (do
        (println "Found multiple versions of cider.el. Will use first." e)
        (doseq [n f] 
          (println (.getName n)))
        (first f))

      :else 
      (first f))
    ))

(defn cider-version 
  "Try to figure out which cider version is installed inside .emacs.d"
  []
  (let [h (System/getProperty "user.home")
        e (str h "/.emacs.d")
        d (file-seq (java.io.File. e))
        f (filter #(= "cider.el" (.getName %)) d)
        ]
    (if (> 1 (count f))
      (do
        (println "Found multiple versions of cider.el. Will use first." e)
        (doseq [n f] 
          (println (.getName n)))))

    (if (first f))

))

(deftask cider
  "Call this before calling the repl task in order to setup
  cider-nrepl and refactor-nrepl middleware."
  [c cider    VERSION str "cider/cider-nrepl version number, defaults to 0.9.1"
   r refactor VERSION str "refactor-nrepl version number, defaults to 1.1.0"]
  (let [c (or cider "0.9.1")
        r (or refactor "1.1.0")]
    (fn middleware [next-handler]
      (fn handler [fileset]
        (set-env! :dependencies 
                  (-> (get-env :dependencies) 
                      (conj ['cider/cider-nrepl c])
                      (conj ['refactor-nrepl r])))
        (swap! boot.repl/*default-dependencies* 
               conj
               ['refactor-nrepl r]
               ['cider/cider-nrepl c])
        (swap! boot.repl/*default-middleware* 
               conj 
               'cider.nrepl/cider-middleware 
               'refactor-nrepl.middleware/wrap-refactor)
        (next-handler fileset)))))
