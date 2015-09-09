(ns upgradingdave.boot-expect
  (:require [boot.core :as core]
            [boot.pod  :as pod]))

(def pod-deps
  '[[expectations "2.1.1"]])

(core/deftask expect
  "Run Expectation Tests in a pod.

  The --namespaces option specifies the namespaces to test. The
  default is to run expectations in all namespaces found in the
  project.

  The --filters option specifies Clojure expressions that are
  evaluated with % bound to a Var representing a namespace under test.
  All must evaluate to true for a Var to be considered for testing.

  I borrowed heavily from boot-test
  https://raw.githubusercontent.com/adzerk-oss/boot-test/0d01e3444acd3b0d1c9dbffad32021178a1fbf03/src/adzerk/boot_test.clj"
  [n namespaces NAMESPACES #{sym} "The set of namespaces to run expectations from"
   f filters    EXPR       #{edn} "The set of expressions to use to filter namespaces"
   ]
  (let [pool (pod/pod-pool
              (update-in (core/get-env) [:dependencies] into pod-deps))]
    (core/cleanup (pool :shutdown))
    (core/set-env! :source-paths #{"test"})
    (core/with-pre-wrap fileset 
      (let [p (pool :refresh)
            namespaces (or (seq namespaces)
                           (seq (core/fileset-namespaces fileset)))]
        (if (seq namespaces)
          (let [filterf `(~'fn [~'%] (and ~@filters))]
            (doto p 
              (pod/with-eval-in 
                (require '[expectations :refer :all])
                (disable-run-on-shutdown)
                (doseq [ns '~namespaces] (require ns))
                (run-tests (into [] (filter ~filterf '~namespaces)))
                )))))
      fileset))
  )

