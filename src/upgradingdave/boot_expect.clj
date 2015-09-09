(ns upgradingdave.boot-expect
  (:require [boot.core :as core]
            [boot.pod  :as pod]))

    ;;TODO: able to run tests in all ns's
    ;; [org.clojure/tools.namespace "0.2.11"]

(def default-expect-version "2.1.1")

(def pod-deps
  '[[expectations "2.1.1"]])

(core/deftask expect
  "Run Expectation Tests"
  [n namespaces NAMESPACES #{sym} "Namespaces that contain expectations"]
  (let [pool (pod/pod-pool
              (update-in (core/get-env) [:dependencies] into pod-deps))
        namespaces (seq namespaces)]
    (core/cleanup (pool :shutdown))
    (core/set-env! :source-paths #{"test"})
    (core/with-pre-wrap fileset 
      (let [p (pool :refresh)]
        (doto p 
          (pod/with-eval-in 
            (require '[expectations :refer :all])
            (doseq [ns '~namespaces] (require ns))
            (disable-run-on-shutdown)
            (run-tests (into [] '~namespaces))
            )))
      fileset))
  )

(comment
  (core/deftask expect
    [v version    VERSION    str    "Version of expectations library"
     n namespaces NAMESPACES #{sym} "Namespaces containing tests to run"
     ]
    (let [pod-deps '[[expectations "2.1.1"]]
          ;;[['expectations (or version default-expect-version)]]
          pool (pod/pod-pool
                (update-in (core/get-env) [:dependencies] into pod-deps))
          namespaces (seq namespaces)
          ]
      (core/cleanup (pool :shutdown))
      (if (empty? namespaces)
        (println "No namespaces to test?")
        (do
          (core/set-env! :source-paths #{"test"})
          (core/with-pre-wrap fileset 
            (let [p (pool :refresh)]
              (doto p
                (pod/with-eval-in
                  (require '[expectations :refer :all])
                  ;;(doseq [ns '~namespaces] (println ns))
                  ;; (disable-run-on-shutdown)
                  ;; (run-tests '[upgradingdave.expect-test])
                  )
                ))
            fileset))))))
