(ns virtual-assistant.commands
  (:require [virtual-assistant.state :refer [app-state widget-data]]
            [clojure.string :as string]))

(def commands
  [{:command #"open youtube"
    :callback (fn [] (swap! widget-data assoc 4 {:key 4 :height 500}))}
   {:command #"close video"
    :callback (fn [] (swap! widget-data dissoc 4))}
   {:command #"search for (.*)"
    :callback
      (fn [query]
        (let [s-query (string/replace query #" " "+")]
          (js/console.log s-query)
          (. js/window (open (str "http://google.com/search?q=" s-query)))))}])
