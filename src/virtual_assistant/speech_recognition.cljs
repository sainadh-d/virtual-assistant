(ns virtual-assistant.speech-recognition
  (:require ["regenerator-runtime/runtime"]
            ["react-speech-recognition" :default SpeechRecognition :refer [useSpeechRecognition]]
            ["@material-ui/core/IconButton" :default IconButton]
            ["@material-ui/icons/MicNoneOutlined" :default MicNoneOutlinedIcon]
            [virtual-assistant.state :refer [app-state]]
            [reagent.core :as reagent]))

(defn keylistener [e]
  (when (and (.-shiftKey e) (= "L" (.-key e)))
    (do
      (swap! app-state assoc :listening true)
      (swap! app-state assoc :status :listening)
      (.startListening SpeechRecognition))))

(defn speech-recognition-component []
  (let [{:keys [transcript listening]} (js->clj (useSpeechRecognition) :keywordize-keys true)]
    ;; Whenever transcript changes the component reloads
    ;; So upon reload set the transcript value to the atom
    ;; But when only update the atom when listening is complete which means user has completed speaking.
    (swap! app-state assoc :transcript transcript)
    (if listening
      (do
        (swap! app-state assoc :listening true)
        (swap! app-state assoc :status :listening))
      ; else
      (do
        (when-not (= transcript "")
          (swap! app-state assoc :final-text transcript)
          (swap! app-state update :history conj transcript))
        (swap! app-state assoc :listening false)
        (swap! app-state assoc :status :idle)))
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
    ;                    (.startListening SpeechRecognition)))}
    ;     [:> MicNoneOutlinedIcon {:fontSize "large"}]]])))
