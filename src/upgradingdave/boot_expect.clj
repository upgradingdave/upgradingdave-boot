(ns upgradingdave.boot-expect
  (:require [boot.core :as core]
            [boot.pod  :as pod]))

(def pod-deps
  '[[expectations "2.1.1"]])

;; TODO if no namespaces are passed on commandline, then run all tests
(core/deftask expect
  "Run Expectation Tests"
  [n namespaces NAMESPACES #{sym} "Namespaces that contain expectations"]
  (let [pool (pod/pod-pool
              (update-in (core/get-env) [:dependencies] into pod-deps))]
    (core/cleanup (pool :shutdown))
    (core/set-env! :source-paths #{"test"})
    (core/with-pre-wrap fileset 
      (let [p (pool :refresh)
            namespaces (or (seq namespaces)
                           (seq (core/fileset-namespaces fileset)))]
        (doto p 
          (pod/with-eval-in 
            (require '[expectations :refer :all])
            (disable-run-on-shutdown)
            (doseq [ns '~namespaces] (require ns))
            (run-tests (into [] '~namespaces))
            )))
      fileset))
  )

