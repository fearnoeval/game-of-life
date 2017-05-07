(ns game-of-life.logic
  (:require
    [clojure.set :as cset]))

(defn make-board
  ([width height grid]
    (make-board width height grid grid))
  ([width height grid changes]
    {:width width :height height :grid grid :changes changes}))

(defn- infinite-random-pairs [width height]
  (repeatedly #(vector (rand-int width) (rand-int height))))

(defn make-random-board [width height]
  (let [take-amount (/ (* width height) 2)
        grid (set (take take-amount (infinite-random-pairs width height)))]
    (make-board width height grid)))

(defn- get-neighbors-for-point [{:keys [width height]} [x y]]
  (map (fn [[x y]] [(mod x width) (mod y height)])
       [[(dec x) (dec y)] [x  (dec y)] [(inc x) (dec y)]
        [(dec x)      y ]              [(inc x)      y ]
        [(dec x) (inc y)] [x  (inc y)] [(inc x) (inc y)]]))

(defn- count-live-neighbors [board point]
  (count (filter #(contains? (:grid board) %) (get-neighbors-for-point board point))))

(defn- will-be-alive? [board point]
  (let [currently-alive?    (contains? (:grid board) point)
        live-neighbor-count (count-live-neighbors board point)]
    (or (and currently-alive? (or (= live-neighbor-count 2) (= live-neighbor-count 3)))
        (and (not currently-alive?) (= live-neighbor-count 3)))))

(defn step [{:keys [grid changes] :as board}]
  (let [changes     (if changes changes grid)
        checklist   (set (mapcat #(get-neighbors-for-point board %) changes))
        safe-list   (cset/difference grid checklist)
        new-grid    (cset/union safe-list (set (filter #(will-be-alive? board %) checklist)))
        new-changes (cset/union (cset/difference new-grid grid) (cset/difference grid new-grid))]
    (assoc board :changes new-changes :grid new-grid)))
