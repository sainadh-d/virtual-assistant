(ns virtual-assistant.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.dom :refer [render]]
            ["regenerator-runtime/runtime"]
            ["@arwes/core" :refer [ArwesThemeProvider StylesBaseline FrameBox Text Button]]
            ["@arwes/animation" :refer [AnimatorGeneralProvider]]
            ["@material-ui/core/TextField" :default TextField]
            ["react-speech-recognition" :default SpeechRecognition :refer [useSpeechRecognition]]
            ["@material-ui/core/InputAdornment" :default InputAdornment]
            ["@material-ui/core/Input" :default Input]
            ["@material-ui/core/IconButton" :default IconButton]
            ["@material-ui/core/FormControl" :default FormControl]
            ["@material-ui/icons/Search" :default SearchIcon];
            ["@material-ui/icons/MicNoneOutlined" :default MicNoneOutlinedIcon]
            ["react-youtube" :default Youtube]
            ["react-grid-layout" :as GridLayout]
            ["@use-it/event-listener" :as useEventListener]
            [clojure.string :as string]))

(defonce app-state (atom {:final-text "" :transcript "" :typed-text "" :listening false :youtube false}))

(def commands
  [{:command "Open Youtube"
    :callback (fn [_ _ _] (swap! app-state assoc :youtube true))}
   {:command "Search for *"
    :callback
      (fn [query]
        (let [s-query (string/replace query #" " "+")]
          (. js/window (open (str "http://google.com/search?q=" s-query)))))}])

(defn keylistener [e]
  (if (and (.-shiftKey e) (= "L" (.-key e)))
    (do
      (js/console.log "cmd u, so listening")
      (swap! app-state assoc :listening true)
      (.startListening SpeechRecognition))))

(defn speech-recognition-component []
  (let [{:keys [transcript listening]} (js->clj (useSpeechRecognition (clj->js {:commands commands})) :keywordize-keys true)]
    ;; Whenever transcript changes the component reloads
    ;; So upon reload set the transcript value to the atom
    ;; But when only update the atom when listening is complete which means user has completed speaking.
    (swap! app-state assoc :transcript transcript)
    (when-not listening
      (swap! app-state assoc :final-text transcript)
      (swap! app-state assoc :listening false))
    ; (useEventListener
    ;   "keydown"
    ;   (fn [e]
    ;     (if (and (.-shiftKey e) (= "L" (.-key e)))
    ;       (do
    ;         (js/console.log "cmd u, so listening")
    ;         (swap! app-state assoc :listening true)
    ;         (.startListening SpeechRecognition)))))
    (reagent/as-element
      (reagent/with-let [_ (js/document.addEventListener "keydown" keylistener)]
        [:div]))))
    ; (reagent/as-element
    ;   [:div
    ;    [:> IconButton
    ;     {:classes {:root "play-btn"}
    ;      :on-click (fn []
    ;                  (do
    ;                    (swap! app-state assoc :listening true)
    ;                    (.startListening SpeechRecognition)))
    ;      :on-key-down (fn [e]
    ;                       (if (and (.-shiftKey e) (= "L" (.-key e)))
    ;                         (do
    ;                           (js/console.log "cmd u, so listening")
    ;                           (swap! app-state assoc :listening true)
    ;                           (.startListening SpeechRecognition))))}
    ;     [:> MicNoneOutlinedIcon {:fontSize "large"}]]])))


(defn searchbar []
  [:div.searchbar
   [:div.search-icon
    [:> SearchIcon {:fontSize "large"}]]
   [:input.searchbar-input
    {:type "text"
     :on-key-down (fn [e]
                    (if (= (.-key e) "Enter")
                      (swap! app-state assoc :typed-text (.. e -target -value))))}]])

(defn text []
  [:> FrameBox
   [:div {:style {:width 400 :height 600}}
    [:> Text (:transcript @app-state)]]])

(defn youtube-video []
  (let [options {:height "400" :width "640"}]
    [:> AnimatorGeneralProvider {:animator {:duration {:enter 500}}}
     [:> FrameBox {:animator {:activate true}}
      [:div
       [:> Youtube
         {:videoId "PPqnC2DPrU8"
          ; :opts options
          :onReady (fn [e] (.pauseVideo (.-target e)))}]]]]))


(defn generate-grid-layout [grid-layout comps]
  (vec (concat grid-layout comps)))

(defn grid-layout []
  [:div.widget-area
   (generate-grid-layout
     [:> GridLayout
         {:className "layout"
          :cols 12
          :rowHeight 30
          :width 2000
          :useCSSTransforms true
          :autoSize true
          :isResizable false}]
     (cond
       (= (:youtube @app-state) true)
       [[:div {:key "a" :data-grid {:x 0 :y 0 :w 1 :h 1}} [youtube-video]] [:div {:key "b" :data-grid {:x 5 :y 2 :w 1 :h 1}} [text]]]
       :else
       [[:div {:key "a" :data-grid {:x 0 :y 0 :w 1 :h 1}} [text]]]))])


(defn listener-executor []
  (cond
    (= (:final-text @app-state) "Open Quora")
    (do
      (.open js/window "https://quora.com" "_blank")))
  [:div])

(defn listener-comp []
  (.addEventListener js/document "keydown" keylistener)
  [:div])

(defn start []
  (render
    [:> ArwesThemeProvider
     [:> StylesBaseline
      [:div
       [:> speech-recognition-component]
       ; [listener-comp]
       [listener-executor]
       [searchbar]
       [grid-layout]]]]
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
