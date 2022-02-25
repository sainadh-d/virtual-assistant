; ["mauerwerk" :refer [Grid]]
; (def widget-map
;   {1 [command-history]
;    2 [text-2]
;    3 [text-2]
;    4 [text-2]
;    5 [youtube-video]
;    6 [text-2]})
;
; (def widget-data
;   (atom
;     [{:key 1
;       :height 700}
;      {:key 2
;        :height 300}
;      {:key 3
;        :height 300}
;      {:key 4
;        :height 300}
;      {:key 5
;        :height 500}]))
;
; (defn renderer [props]
;   (let [properties (js->clj props :keywordize-keys true)
;         key (:key properties)]
;     (r/as-element (get widget-map key))))
;
; (defn widget-area []
;   [:div.widget-area
;    [:> Grid
;     {:data @widget-data
;      :keys (fn [d]
;              (:key (js->clj d :keywordize-keys true)))
;      :heights (fn [d]
;                 (:height (js->clj d :keywordize-keys true)))
;      :columns 4
;      :margin 1
;      :transitionMount true}
;     renderer]])

; (defn widget-data []
;   [{:key 1 :height 700 :title "Hello"}
;    {:key 2 :height 300 :title "World"}])
;
; (defn convert-js-clj [object]
;  (js->clj object :keywordize-keys true))
;
; (defn test-component []
;  [:> Grid
;    {:data       widget-data
;     :keys       (fn [d] (:key (convert-js-clj d)))
;     :heights    (fn [d] (:height (convert-js-clj d)))
;     :columns    3
;     :margin     0
;     :lockScroll false
;     :closeDelay 500}
;    (fn [props]
;     (let [properties (convert-js-clj props)
;           data       (:data properties)
;           open       (:open properties)
;           toggle     (:toggle properties)]
;       (reagent/as-element
;         [:div
;           (:title data)])))])
