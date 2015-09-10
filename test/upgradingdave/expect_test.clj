(ns upgradingdave.expect-test
  (:require [expectations :refer [expect]]))

(expect nil? nil)

(expect "sanity check to see if expect task is working" 
        "sanity check to see if expect task is working")



