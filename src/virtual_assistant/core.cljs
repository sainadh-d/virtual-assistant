(ns virtual-assistant.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.dom :refer [render]]
            ["@arwes/core" :refer [ArwesThemeProvider StylesBaseline FrameBox Text Button]]
            ["@arwes/animation" :refer [AnimatorGeneralProvider]]
            ["@material-ui/core/Grid" :default Grid]
            ["react-grid-layout" :refer [Responsive WidthProvider] :as GridLayout]
            ["react-websocket" :as Websocket]
            [clojure.string :as string]
            [virtual-assistant.state :refer [app-state stats]]
            [virtual-assistant.commands :refer [commands]]
            [virtual-assistant.topbar :refer [top-layout]]
            [virtual-assistant.widgets :refer [widget-area]]))

(defn command-executor []
  (let [input (string/lower-case (:final-text @app-state))]
    (doseq [command commands]
      (let [regex-command (:command command)
            callback      (:callback command)
            matches       (re-matches regex-command input)]
          (when matches
            (if (vector? matches)
              (apply callback (subvec matches 1))
              ;; else
              (callback))))))
  [:div])

(defn speech-ws-listener []
  [:> Websocket
   {:url "ws://127.0.0.1:8443/speech"
    :onMessage (fn [data]
                 (swap! app-state assoc :transcript data)
                 (swap! app-state assoc :final-text data)
                 (swap! app-state update :history conj data))
    :onOpen (fn [] (swap! app-state assoc :status :listening))
    :onClose (fn [] (swap! app-state assoc :status :idle))}])

(defn stats-ws-listener []
  [:> Websocket
   {:url "ws://127.0.0.1:8444"
    :onMessage
         (fn [data]
           (let [parsed-data (.parse js/JSON data)
                 clj-data (js->clj parsed-data)
                 cpu (get clj-data "cpu")
                 ram (get clj-data "ram")]
              (swap! stats assoc :cpu cpu)
              (swap! stats assoc :ram ram)))
    :onOpen (fn [] (js/console.log "time socket opened"))
    :onClose (fn [] (js/console.log "time socket closed"))}])

; prev color #31afe0 #07C3E2 #07C4E2
(def arwes-theme-settings
  {:palette
    {:primary
      {:main "#07C3E2"}
     ; :secondary
     ;  {:main "#8F002F"}
     :text
      {:root "#13E7FF"}}})

(defn start []
  (render
    [:> ArwesThemeProvider {:themeSettings arwes-theme-settings}
     [:> StylesBaseline
      [:div
       [speech-ws-listener]
       [stats-ws-listener]
       [command-executor]
       [top-layout]
       [widget-area]]]]
    (. js/document (getElementById "app"))))


(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

(defn stop []
  ;; stop is called before any code is reloaded
  ;; this is controlled by :before-load in the config
  (js/console.log "stop"))
