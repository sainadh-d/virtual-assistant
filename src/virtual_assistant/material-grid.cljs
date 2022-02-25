;; Material Grid

; (defn widgets []
;   [[:> Grid {:key "b" :item true}
;     [command-history]]
;    [:> Grid {:key "a" :item true}
;     [text-2]]
;    [:> Grid {:key "c" :item true}
;     [text]]])
;
; (defn widget-area []
;   [:div.widget-area
;     (generate-grid-layout
;       [:> Grid
;         {:container true
;          :justifyContent "flex-start"
;          :alignItems "flex-start"
;          :direction "row"
;          :wrap "wrap"
;          :spacing 2}]
;       (widgets))])
