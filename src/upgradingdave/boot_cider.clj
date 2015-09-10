(ns upgradingdave.boot-cider
  (:require [boot.core :as core]
            [boot.repl :as repl ]))

(defn find-file 
  "Search .emacs.d directory for an elisp file"
  [fname]
  (let [fs  (-> (System/getProperty "user.home")
                (str "/.emacs.d")
                (java.io.File.)
                file-seq)
        fs (filter #(= fname (.getName %)) fs)]
    (cond

      (empty? fs)
      nil

      (> 1 (count fs))
      (do
        (println "Found multiple versions of" fname)
        (first fs))

      :else 
      (first fs))
    ))

(defn cider-version 
  "Try to figure out which cider version is installed inside .emacs.d"
  []
  (if-let [f (find-file "cider.el")]
    (second (re-find #".*cider-version \"(.*)\"" (slurp f)))))

(defn clj-refactor-version 
  "Try to figure out which cider version is installed inside .emacs.d"
  []
  (if-let [f (find-file "clj-refactor.el")]
    (second (re-find #".*cljr-version \"(.*)\"" (slurp f)))))

(core/deftask cider
  "Call this before calling the repl task in order to setup
  cider-nrepl and refactor-nrepl middleware."
  [c cider    VERSION str "cider-nrepl version number. By default, this will try and figure out the version by looking inside .emacs.d"
   r refactor VERSION str "clj-refactor version number. By default, this will try and figure out the version by looking inside .emacs.d"]
  (let [c (or cider (cider-version) "0.9.1")
        r (or refactor (clj-refactor-version) "1.1.0")]
    (fn middleware [next-handler]
      (fn handler [fileset]
        (core/set-env! :dependencies 
                  (-> (core/get-env :dependencies) 
                      (conj ['cider/cider-nrepl c])
                      (conj ['refactor-nrepl r])))
        (swap! repl/*default-dependencies* 
               conj
               ['refactor-nrepl r]
               ['cider/cider-nrepl c])
        (swap! repl/*default-middleware* 
               conj 
               'cider.nrepl/cider-middleware 
               'refactor-nrepl.middleware/wrap-refactor)
        (next-handler fileset)))))
