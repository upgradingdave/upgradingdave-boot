#!/usr/bin/env boot

(set-env!
 :dependencies '[[org.clojure/clojure "1.7.0" :scope "provided"]
                 [hiccup "1.0.5"]
                 ]
 :source-paths #{"src"}
 )

(def project 
  {:id "boot-sitegen"
   :version "0.1.0"
   :group-id "com.upgradgindave"
   })

;; Custom boot tasks
(require ;;'[upgradingdave.boot-cider :refer :all]
         '[upgradingdave.boot-expect :refer :all])

;; (task-options!
;;   cider {:cider "0.10.0-SNAPSHOT"
;;          :refactor "1.2.0-SNAPSHOT"})


;; (deftask sitegen
;;   "Create static html site"
;;   [d dir DIRECTORY file "Directory that contains markdown files"]
;;   (let []

;;     (fn middleware [next-handler]

;;       (fn handler [fileset]

;;         (let [fileset' fileset
;;               fileset' (commit! fileset')
;;               result (next-handler fileset')]
          
;;           result)))))

