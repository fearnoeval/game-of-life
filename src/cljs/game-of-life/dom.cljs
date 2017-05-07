(ns game-of-life.dom
  (:require
    [goog.dom]))

(defn get-element [element-name]
  (goog.dom/getElement (name element-name)))

(defn get-viewport-size []
  (let [viewport-size (goog.dom/getViewportSize)]
    {:height (.-height viewport-size)
     :width  (.-width viewport-size)}))

(defn set-text-content! [element text-content]
  (goog.dom/setTextContent element text-content))
