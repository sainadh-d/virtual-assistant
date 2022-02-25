(ns virtual-assistant.state
  (:require [reagent.core :as reagent :refer [atom]]))

(defonce app-state
  (atom
    {:final-text ""
     :status :idle
     :history []}))

(defonce stats
  (atom
    {:cpu 0
     :ram 0}))

(defonce widget-data
  (atom
    {1 {:key 1
        :height 700
        :width 1}
     6 {:key 6
        :height 300
        :width 1}
     2 {:key 2
         :height 300
         :width 1}
     4 {:key 4
        :height 500
        :width 2}
     3 {:key 3
        :height 800
        :width 1}
     5 {:key 5
        :height 300
        :width 1}
     7 {:key 7
        :height 100
        :width 1}}))
