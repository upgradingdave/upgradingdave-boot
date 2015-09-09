(ns upgradingdave.expect-test
  (:require [expectations :refer [expect]]))

(expect nil? nil)

(expect "hi" "hi")

(expect "awesome!" "awesome!")

(expect "nice!" "nice!")


