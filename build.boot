#!/usr/bin/env boot

(set-env!
 :dependencies '[[org.clojure/clojure "1.7.0" :scope "provided"]
                 [boot/core           "2.2.0" :scope "provided"]]
 :source-paths #{"src"}
 )

(def project 
  {:id "boot-sitegen"
   :version "0.1.0"
   :group-id "com.upgradgindave"
   })

;; Custom boot tasks
(require '[upgradingdave.boot-expect :refer :all])

(deftask dev []
  (merge-env! :source-paths #{"test"})
  (comp (watch)
        (speak)
        (expect)))
