#!/usr/bin/env boot

(merge-env!
 :dependencies '[[org.clojure/clojure "1.7.0"  :scope "provided"]
                 [boot/core           "2.2.0"  :scope "provided"]]

 :source-paths #{"src"}
 )

(require '[upgradingdave.boot-expect :as e]
         '[upgradingdave.boot-cider  :refer :all])

(def +version+ "0.1.0")

(task-options!
 pom {:project 'upgradingdave/boot-dave
      :version +version+
      :description "Dave's custom boot tasks"})

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

(deftask install-clj 
  "Create pom, build jar and install to local maven repo. Remember
  that stuff on source-paths is not treated as output. So, here we put
  everything in src in resource-paths so that it's included inside the jar"
  []
  (merge-env! :resource-paths #{"src"})
  (comp 
   (pom)
   (jar)
   (install)))

