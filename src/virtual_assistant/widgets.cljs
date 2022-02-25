(ns virtual-assistant.widgets
  (:require ["react-youtube" :default Youtube]
            [reagent.core :as r :refer [atom]]
            ["@arwes/core" :refer [FrameBox Text]]
            ["@arwes/core" :refer [ArwesThemeProvider StylesBaseline FrameBox FrameCorners Text Button]]
            ["@arwes/animation" :refer [AnimatorGeneralProvider]]
            ["react-grid-layout" :refer [Responsive WidthProvider] :as GridLayout]
            ["mauerwerk" :refer [Grid]]
            ["react-circle" :default Circle]
            ["react-interval" :as ReactInterval]
            ["react-xmasonry" :refer [XMasonry, XBlock]]
            [virtual-assistant.state :refer [app-state widget-data stats]]
            ["react-vis" :refer [XYPlot XAxis YAxis HorizontalGridLines VerticalBarSeries VerticalGridLines AreaSeries LineSeries]]));

(def chart-data
  [{:x 9, :y 0}
   {:x 0, :y 8}
   {:x 1, :y 5}
   {:x 2, :y 4}
   {:x 3, :y 9}
   {:x 4, :y 1}
   {:x 5, :y 7}
   {:x 6, :y 6}
   {:x 7, :y 3}
   {:x 8, :y 2}])

(defn test-chart []
  [:> AnimatorGeneralProvider {:animator {:duration {:enter 3500}}}
   [:> FrameCorners {:showContentLines true :cornerWidth 2}
    [:div.header.heading "STATS"]
    [:> XYPlot {:width 400 :height 300}
     [:> LineSeries {:data chart-data :curve "curveBasis" :color "#07C3E2" :animation {:damping 9, :stiffness 300}}]
     ; [:> LineSeries {:data horizontal-line :curve "curveBasis" :color "red"}]
     ; [:> HorizontalGridLines]
     ; [:> VerticalGridLines]
     [:> XAxis]
     [:> YAxis]]]])

(defn speech-pad []
  [:> AnimatorGeneralProvider {:animator {:duration {:enter 3500}}}
   [:> FrameCorners {:showContentLines true :cornerWidth 2}
    [:div.header.heading "SPEECH PAD"]
    [:div
     {:style {:font-size 25 :width "400px" :height "200px" :padding "5px"}}
     (:final-text @app-state)]]])

;; https://stackoverflow.com/questions/49425141/how-to-plot-time-series-graph-using-react-vis
(def time-series-data
  (atom
    [{:x (js/Date. "01/01/2021 19:21:01"), :y 75,}
     {:x (js/Date. "01/01/2021 19:21:02"), :y 60,}
     {:x (js/Date. "01/01/2021 19:21:03"), :y 80,}
     {:x (js/Date. "01/01/2021 19:21:04"), :y 90}]))

(defn time-series-chart []
  [:> AnimatorGeneralProvider {:animator {:duration {:enter 3500}}}
   [:> FrameCorners {:showContentLines true :cornerWidth 2}
    [:div.header.heading "TIME SERIES"]
    [:> XYPlot {:width 400 :height 200 :xType "time"}
     [:> LineSeries {:data @time-series-data :curve "curveBasis" :color "#07C3E2" :animation {:damping 9, :stiffness 300}}]
     [:> XAxis]
     [:> YAxis]]]])

(defn time-pad []
  [:> AnimatorGeneralProvider {:animator {:duration {:enter 3500}}}
   [:> FrameCorners {:showContentLines true :cornerWidth 2 :animator {:activate true}}
    [:div.header.heading "TIME PAD"]
    [:div {:style {:font-size 25 :width "400px" :height "200px" :padding "5px"}}
     [:> Text (:time @app-state)]]]])

(defn command-history-items [commands]
  (for [command (take 15 (reverse (remove empty? commands)))]
    ^{:key (random-uuid)} ; Todo: Can we change this from using random-uuid
    [:div.history-item command]))

(defn command-history []
  [:> AnimatorGeneralProvider {:animator {:duration {:enter 3500}}}
    [:> FrameCorners {:showContentLines true :cornerWidth 2 :animator {:activate true}}
     [:div.command-history {:style {:width "400px" :height "700px" :padding "5px" :word-wrap "break-word"}}
      [:div.header.heading "COMMAND HISTORY"]
      [:div.history-items
       (command-history-items (:history @app-state))]]]])

(defn youtube-video []
  (let [options {:height "500" :width "800"}]
    [:> AnimatorGeneralProvider {:animator {:duration {:enter 3500}}}
     [:> FrameCorners {:showContentLines true :cornerWidth 2 :animator {:activate true}}
      [:div
       [:> Youtube
         {:opts options
          ; :videoId "PPqnC2DPrU8"
          :videoId "bwmSjveL3Lc"
          :onReady (fn [e] (.pauseVideo (.-target e)))}]]]]))

(def cpu-usage (atom 89.1))

(defn circle-test []
  [:div {:style {:opacity 0.8 :text-align "center"}}
   [:> ReactInterval {:timeout 1000 :enabled true :callback (fn [] (reset! cpu-usage (rand-int 90)))}]
   [:div.heading {:style {:font-size 20}} "CPU Usage"]
   ; [:div {:style {:margin-top -20}}
   [:> Circle
    {:animate true
     :animationDuration "1s"
     :size 100
     :progress (:cpu @stats)
     :roundedStroke true
     :textColor "#07C3E2"
     :progressColor "#07C3E2"
     :bgColor "#"
     :textStyle {:font "bold 5rem Iceland"}
     :showPercentageSymbol false}]
   [:div.heading {:style {:font-size 20}} "RAM Usage"]
   [:> Circle
    {:animate true
     :animationDuration "1s"
     :size 100
     :progress (:ram @stats)
     :roundedStroke true
     :textColor "#07C3E2"
     :progressColor "#07C3E2"
     :bgColor "#"
     :textStyle {:font "bold 5rem Iceland"}
     :showPercentageSymbol false}]])

(def widget-map
  {1 [command-history]
   2 [speech-pad]
   3 [circle-test]
   4 [youtube-video]
   5 [time-pad]
   6 [test-chart]
   7 [time-series-chart]})
;
;
; (defn renderer [props]
;   (let [properties (js->clj props :keywordize-keys true)
;         key (:key properties)]
;     (r/as-element (get widget-map key))))
;
; (defn widget-area []
;   [:div.widget-area
;    [:> Grid
;     {:data (vec (vals @widget-data))
;      :keys (fn [d]
;              (:key (js->clj d :keywordize-keys true)))
;      :heights (fn [d]
;                 (:height (js->clj d :keywordize-keys true)))
;      :columns 4
;      :margin 20
;      :transitionMount true}
;     renderer]])

; CSS Flex Box
; (defn widget-area []
;   [:div.widget-area
;    (for [widget-data (vec (vals @widget-data))]
;      (let [key (:key widget-data)]
;        [:div
;         (get widget-map key)]))])

; XMasonry
(defn widget-area []
  [:div.widget-area
   [:> XMasonry {:targetBlockWidth 400 :responsive true}
     (for [widget-data (vec (vals @widget-data))]
       (let [key (:key widget-data)
             width (:width widget-data)]
         [:> XBlock {:width width}
          [:div.card
            (get widget-map key)]]))]])
