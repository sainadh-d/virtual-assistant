(ns virtual-assistant.topbar
  (:require [reagent.core :refer [atom]]
            ["@arwes/core" :refer [FrameBox FrameLines]]
            ["react-live-clock" :as Clock]
            ["@material-ui/core/Grid" :default Grid]
            ["@material-ui/icons/Search" :default SearchIcon]
            [virtual-assistant.state :refer [app-state]]
            [clojure.string :as string]
            [ajax.core :refer [POST]]))

(defn time-widget []
  [:> FrameLines
   [:div.time-widget
    [:> Clock
     {:format "dddd - MMMM Mo, YYYY - h:mm:ss A"
      :ticking true
      :filter (fn [date] (string/upper-case date))}]]])

(defn status []
  [:div.status-bar
   [:> FrameLines {:palette "secondary"}
    (let [st (:status @app-state)]
      [:div {:class
              (if (= st :listening)
                "status blink"
                ; else
                "status")}
        (string/upper-case (name st))])]])

(defn response-handler [response]
  (js/console.log (:response response))
  (swap! app-state assoc :final-text (:response response)))

(defn call-api [query]
  (POST "http://127.0.0.1:5000/"
    {:params
     {:query query}
     :format :json
     :response-format :json
     :keywords? true
     :handler response-handler}))

(defn searchbar []
  [:div.searchbar
   [:div.search-icon
    [:> SearchIcon {:fontSize "large"}]]
   [:input.searchbar-input
    {:type "text"
     :on-key-down
       (fn [e]
         (when (= (.-key e) "Enter")
           (let [query (.. e -target -value)]
             (swap! app-state assoc :final-text query)
             (swap! app-state update :history conj query)
             (call-api query))))}]])

(defn top-layout []
  [:div.top-layout
   [:> Grid
     {:container true
      :justifyContent "center"
      :alignItems "center"
      :direction "row"
      :wrap "wrap"
      :spacing 9}
    [:> Grid {:key "time-widget" :item true}
     [time-widget]]
    [:> Grid {:key "searchbar" :item true}
     [searchbar]]
    [:> Grid {:key "status" :item true}
     [status]]]])
