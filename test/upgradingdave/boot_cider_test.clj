(ns upgradingdave.boot-cider-test
  (:require [upgradingdave.boot-cider :refer :all]
            [expectations :refer [expect]]))

(expect "cider.el" (.getName (find-file "cider.el")))
(expect "clj-refactor.el" (.getName (find-file "clj-refactor.el")))
(expect "0.10.0-snapshot" (cider-version))
(expect "1.2.0-SNAPSHOT" (clj-refactor-version))

