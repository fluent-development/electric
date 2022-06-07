(ns user.demo-2-system-properties
  (:require [clojure.string :as str]
            [hyperfiddle.photon :as p]
            [hyperfiddle.photon-dom :as dom])
  (:import (hyperfiddle.photon Pending))
  #?(:cljs (:require-macros user.demo-2-system-properties)))

(defn system-properties [?s]
  #?(:clj (->> (System/getProperties)
               (filter (fn [[k v]] (str/includes? (str/lower-case (str k)) (str/lower-case (str ?s)))))
               (into {}))))

(p/defn Input []
  (dom/input {:type :search, :placeholder "Filter…"}
             (dom/events "input" (map (dom/oget :target :value)) "")))

(p/defn App []
  (dom/div
    (dom/h1 (dom/text "System Properties"))
    (let [filter (Input.)]
      (dom/div (dom/text (str "Input: " filter)))
      (dom/table
        ~@(p/for [[k v] (sort-by key (system-properties filter))]
            ~@(dom/tr
                (dom/td (dom/text (str k)))
                (dom/td (dom/text (str v)))))))))

(def main #?(:cljs (p/client (p/main
                               (try
                                 (binding [dom/node (dom/by-id "root")]
                                   (App.))
                                 (catch Pending _))))))

(comment
  (user/browser-main! `main)
  )