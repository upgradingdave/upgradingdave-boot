(ns upgradingdave.boot-cider
  (:require [boot.core :as core]
            [boot.repl :as repl ]))

(defn find-elpa-file 
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
  (if-let [f (find-elpa-file "cider.el")]
    (if-let [v (second (re-find #".*cider-version \"(.*)\"" (slurp f)))]
      (.toUpperCase v))))

(defn clj-refactor-version 
  "Try to figure out which cider version is installed inside .emacs.d"
  []
  (if-let [f (find-elpa-file "clj-refactor.el")]
    (if-let [v (second (re-find #".*cljr-version \"(.*)\"" (slurp f)))]
      (.toUpperCase v))))

(defn- add-dep [dep ver middleware]
  (core/set-env! :dependencies 
                 (-> (core/get-env :dependencies) 
                     (conj [dep ver])))

  (swap! repl/*default-dependencies* 
         conj
         [dep ver])

  (swap! repl/*default-middleware* 
         conj 
         middleware))

(core/deftask cider
  "Call this before calling the repl task in order to setup
  cider-nrepl and refactor-nrepl middleware."
  [c cider    VERSION str "cider-nrepl version number. By default, this will try and figure out the version by looking inside .emacs.d"
   r refactor VERSION str "clj-refactor version number. By default, this will try and figure out the version by looking inside .emacs.d"]
  (let [cver (or cider (cider-version) "0.9.1")
        rver (or refactor (clj-refactor-version) "1.1.0")]
    (fn middleware [next-handler]
      (fn handler [fileset]
        (if cver
          (add-dep 'cider/cider-nrepl cver 
                   'cider.nrepl/cider-middleware ))
        (if rver
          (add-dep 'refactor-nrepl rver
                   'refactor-nrepl.middleware/wrap-refactor))
        (next-handler fileset)))))
