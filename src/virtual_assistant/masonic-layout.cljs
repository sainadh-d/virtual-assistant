; (def widgets
;   {1 [command-history]
;    2 [text-2]
;    3 [text-2]
;    4 [text-2]
;    5 [youtube-video]
;    6 [text-2]})
;
; (defn comp-generator [id]
;   (let [key (:id (:data (js->clj id :keywordize-keys true)))]
;     (js/console.log (clj->js id))
;     (get widgets key)))
;
; (defn widget-area []
;   [:div.widget-area
;    [:> Masonry
;     {:items [{:id 1} {:id 2} {:id 3} {:id 4} {:id 5} {:id 6}]
;      :columnWidth 400
;      :render (r/reactify-component comp-generator)
;      :columnGutter 20}]])
