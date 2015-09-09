(ns upgradingdave.boot-expect
  (:require [boot.core :as core]
            [boot.pod  :as pod]))

    ;;TODO: able to run tests in all ns's
    ;; [org.clojure/tools.namespace "0.2.11"]

(defn init [fresh-pod]
  (doto fresh-pod
    (pod/with-eval-in
     (require '[expectations :refer :all]))))

(def pod-deps
  '[[expectations "2.1.1"]])

(core/deftask expect
  []
  (core/set-env! :source-paths #{"test"})
  ;; (core/cleanup (do
  ;;                 (println "cleanup up pods")
  ;;                 (pod/destroy-pod p)) )
  (core/with-pre-wrap fileset 
    (let [p (pod/make-pod 
             (update-in (core/get-env) [:dependencies] into pod-deps))]
      ;; (core/cleanup (do
      ;;                 (println "cleanup up pods")
      ;;                 (pod/destroy-pod p)) )
      (doto p 
        ;;(pod/require-in "expectations")
        (pod/with-eval-in 
          (println "inside a pod!")
          (require '[expectations :refer :all])
          (require '[upgradingdave.expect-test])
          (disable-run-on-shutdown)
          (run-tests '[upgradingdave.expect-test])
          )
        )
      (println "cleaning up pod")
      (pod/destroy-pod p)
      )
    fileset)
  ;;(pod/destroy-pod p)
  )

;; (core/deftask expect
;;   "Run expectations"
;;   [n namespace NAMESPACE sym "Namespace with expectations to run"]
;;   (let [worker-pods (pod/pod-pool 
;;                      (update-in (core/get-env) [:dependencies] into pod-deps) 
;;                      :init init)]
;;     (core/cleanup (worker-pods :shutdown))
;;     (core/set-env! :source-paths #{"test"})

;;     (println "Attempting to run isolated pod")

;;     (core/with-pre-wrap fileset
;;       (println "inside pod!")
;;       (let [worker-pod (worker-pods)]
;;         ;; (pod/with-eval-in worker-pod
;;         ;;   ;;(disable-run-on-shutdown)
;;         ;;   ;;(require '~namespace)
;;         ;;   ;;(run-tests [namespace])
;;         ;;   )
;;         )
;;       fileset)
;;     ))
