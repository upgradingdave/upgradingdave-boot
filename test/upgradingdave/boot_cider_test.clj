(ns upgradingdave.boot-cider-test
  (:require [upgradingdave.boot-cider :refer :all]
            [expectations :refer [expect]]))

(expect "cider.el" (.getName (find-cider-el)))
(expect "0.10.0-snapshot" (cider-version))

