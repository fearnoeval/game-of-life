(ns game-of-life.core
  (:require
    [cljs.core.async :refer [<! >! chan put! timeout]]
    [game-of-life.dom :refer [get-element get-viewport-size set-text-content!]]
    [game-of-life.events :as events]
    [game-of-life.logic :as logic]
    [game-of-life.boards :as boards]
    [goog.style :as gstyle])
  (:import
    [goog.async AnimationDelay]
    [goog.net ImageLoader])
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]]))

(def background-color "#cfad88")
(def foreground-color "#3c2716")
(def scaling-factor 20)
(def canvas-context (.getContext (get-element :the-canvas) "2d"))
(def images
    {:envelope            {:src "envelope.gif"}
     :uncle-buck-original {:src "uncle-buck-original.gif"}
     :uncle-buck-closeup  {:src "uncle-buck-closeup.jpg"}})
(def buffer-size 3)

(defn console-log [& args]
  (.apply (.-log js/console) js/console (into-array args)))

(defn make-state-channel [board]
  (let [channel (chan buffer-size)]
    (go
      (>! channel board)
      (<! (timeout 50))
      (loop [next-state (logic/step board)]
        (<! (timeout 0))
        (>! channel next-state)
        (recur (logic/step next-state))))
    channel))

(defn events->chan
  ([element event-keyword]
    (events->chan element event-keyword (chan)))
  ([element event-keyword channel]
    (events/listen element event-keyword (fn [event] (put! channel event)))
    channel))

(defn load-images [images]
  (let [completion-chan   (chan)
        image-loader      (ImageLoader.)
        image-loader-chan (events->chan image-loader :load)]
    (doseq [[key value] images]
      (.addImage image-loader (name key) (:src value)))
    (.start image-loader)
    (go-loop [n      (count images)
              images images]
      (if (zero? n)
        (>! completion-chan images)
        (let [event (<! image-loader-chan)
              id    (keyword (.. event -target -id))]
          (recur (dec n) (assoc-in images [id :event] event)))))
    completion-chan))

(defn clear-canvas! [board]
  (set! (.-fillStyle canvas-context) background-color)
  (let [canvas-width  (* scaling-factor (:width board))
        canvas-height (* scaling-factor (:height board))]
    (.clearRect canvas-context 0 0 canvas-width canvas-height)))

(defn draw-grid-once! [board]
  (clear-canvas! board)
  (set! (.-fillStyle canvas-context) foreground-color)
  (doseq [[col row] (:grid board)]
    (.fillRect canvas-context (* col scaling-factor) (* row scaling-factor) scaling-factor scaling-factor)))

(defn draw-grid! [draw-chan]
  (go
    (let [state-chan (<! draw-chan)
          state      (<! state-chan)]
      (.setTimeout js/window (fn [_] (.start (AnimationDelay. #(draw-grid! draw-chan)))) 100)
      (draw-grid-once! state)
      (>! draw-chan state-chan))))

(defn draw-image! [image-data board]
  (let [target        (.-target (:event image-data))
        canvas-width  (* scaling-factor (:width board))
        canvas-height (* scaling-factor (:height board))
        image-width   (.-width target)
        image-height  (.-height target)
        halve         #(/ % 2)
        starting-x    (- (halve canvas-width) (halve image-width))
        starting-y    (- (halve canvas-height) (halve image-height))]
    (.drawImage canvas-context target starting-x starting-y image-width image-height)))

(defn play-intro! [state-chan]
  (let [timeout-duration 1800]
    (go
      (let [images (<! (load-images images))
            board  (<! state-chan)]
        (draw-image! (:envelope images) board)
        (<! (timeout timeout-duration))
        (draw-image! (:uncle-buck-original images) board)
        (<! (timeout timeout-duration))
        (draw-image! (:uncle-buck-closeup images) board)
        (<! (timeout timeout-duration))
        (draw-grid-once! board))
      state-chan)))

(defn resize-callback [e]
  (let [canvas                 (get-element :the-canvas)
        canvas-width           (* scaling-factor 30)
        canvas-height          (* scaling-factor 30)
        {window-width :width
        window-height :height} (get-viewport-size)
        padding                40
        minimum-dimension      (min window-width
                                    window-height
                                    (+ canvas-width padding)
                                    (+ canvas-height padding))
        size                   (if (or (< minimum-dimension (+ canvas-width padding))
                                       (< minimum-dimension (+ canvas-height padding)))
                                 (- minimum-dimension padding)
                                 (min canvas-width canvas-height))
        size                   (str size "px")]
    (gstyle/setSize canvas size size)))

(defn start-resize-listener []
  (events/listen js/window :load resize-callback)
  (events/listen js/window :resize resize-callback))

(defn main []
  (start-resize-listener)
  (go
    (let [state-chan              (make-state-channel boards/uncle-buck)
          state-chan              (<! (play-intro! state-chan))
          draw-chan               (chan)
          start-pause-resume      (events->chan (get-element :start-pause-resume) :click)
          reset                   (events->chan (get-element :reset) :click)
          random                  (events->chan (get-element :random) :click)
          set-start-pause-resume! #(set-text-content! (get-element :start-pause-resume) %)]
      (draw-grid! draw-chan)
      (loop [running? false state-chan state-chan]
        (let [[_ source] (alts! [start-pause-resume reset random])]
          (cond
            (or (= source reset) (= source random))
            (let [[message new-state-chan] (if (= source reset)
                                             ["Resetting"               (make-state-channel boards/uncle-buck)]
                                             ["Setting to random board" (make-state-channel (logic/make-random-board 30 30))])]
              (when running? (<! draw-chan))
              (console-log message)
              (draw-grid-once! (<! new-state-chan))
              (set-start-pause-resume! "Start")
              (recur false new-state-chan)))
            (= source start-pause-resume)
            (let [[console-message button-text] (if running?  ["Pausing" "Resume"] ["Starting/Resuming" "Pause"])]
              (console-log console-message)
              (if running?
                (<! draw-chan)
                (>! draw-chan state-chan))
              (set-start-pause-resume! button-text)
              (recur (not running?) state-chan)))))))

(main)
