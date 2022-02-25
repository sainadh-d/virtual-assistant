
; (defn grid-layout []
;   [:div.widget-area
;    (generate-grid-layout
;      [:> GridLayout
;          {:className "layout"
;           :breakpoints {:lg 1600 :md 996 :sm 768 :xs 480 :xxs 0}
;           :cols 12
;           :rowHeight 60
;           :width 1468
;           :useCSSTransforms true
;           :autoSize true
;           :isResizable true}]
;      (cond
;        (= true true)
;        [[:div {:key "a" :data-grid {:x 0 :y 0 :w 2 :h 2}} [youtube-video]]
;         [:div {:key "b" :data-grid {:x 5 :y 2 :w 1 :h 1}} [text]]]
;        :else
;        [[:div {:key "a" :data-grid {:x 0 :y 0 :w 2 :h 2}} [text]]
;         [:div {:key "b" :data-grid {:x 2 :y 0 :w 2 :h 2}} [text]]
;         [:div {:key "c" :data-grid {:x 4 :y 0 :w 2 :h 2}} [text]]
;         [:div {:key "d" :data-grid {:x 6 :y 0 :w 2 :h 2}} [text]]
;         [:div {:key "e" :data-grid {:x 8 :y 0 :w 2 :h 2}} [text]]
;         [:div {:key "f" :data-grid {:x 10 :y 0 :w 2 :h 2}} [text]]]))])


; (defn generate-grid-layout [grid-layout comps]
;   (vec (concat grid-layout comps)))
