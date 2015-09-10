#!/usr/bin/env boot

(set-env!
 :dependencies '[[org.clojure/clojure "1.7.0" :scope "provided"]
                 [boot/core           "2.2.0" :scope "provided"]]
 :source-paths #{"src"}
 )

(require '[upgradingdave.boot-expect :as e]
         '[upgradingdave.boot-cider  :refer :all])

(deftask cider-repl []
  (comp (cider)
        (repl)))

(deftask expect []
  (merge-env! :source-paths #{"test"})
  (e/expect))

(deftask auto-expect []
  (merge-env! :source-paths #{"test"})
  (comp (watch)
        (speak)
        (e/expect)))
