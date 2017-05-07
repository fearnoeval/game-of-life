(ns game-of-life.events
  (:require
    [goog.dom]
    [goog.events]))

(def keyword->event-type
  "Covers all of the goog.events.EventType properties as of 2015-12-18.
   Comments are from the goog.events.EventType source."
  {;; Mouse events
   :click goog.events.EventType.CLICK
   :rightclick goog.events.EventType.RIGHTCLICK
   :dblclick goog.events.EventType.DBLCLICK
   :mousedown goog.events.EventType.MOUSEDOWN
   :mouseup goog.events.EventType.MOUSEUP
   :mouseover goog.events.EventType.MOUSEOVER
   :mouseout goog.events.EventType.MOUSEOUT
   :mousemove goog.events.EventType.MOUSEMOVE
   :mouseenter goog.events.EventType.MOUSEENTER
   :mouseleave goog.events.EventType.MOUSELEAVE
   ;; Select start is non-standard.
   ;; See http://msdn.microsoft.com/en-us/library/ie/ms536969(v=vs.85).aspx.
   :selectstart goog.events.EventType.SELECTSTART

   ;; Wheel events
   ;; http://www.w3.org/TR/DOM-Level-3-Events/#events-wheelevents
   :wheel goog.events.EventType.WHEEL

   ;; Key events
   :keypress goog.events.EventType.KEYPRESS
   :keydown goog.events.EventType.KEYDOWN
   :keyup goog.events.EventType.KEYUP

   ;; Focus
   :blur goog.events.EventType.BLUR
   :focus goog.events.EventType.FOCUS
   :deactivate goog.events.EventType.DEACTIVATE
   ;; NOTE: The following two events are not stable in cross-browser usage.
   ;;     WebKit and Opera implement DOMFocusIn/Out.
   ;;     IE implements focusin/out.
   ;;     Gecko implements neither see bug at
   ;;     https://bugzilla.mozilla.org/show_bug.cgi?id=396927.
   ;; The DOM Events Level 3 Draft deprecates DOMFocusIn in favor of focusin:
   ;;     http://dev.w3.org/2006/webapi/DOM-Level-3-Events/html/DOM3-Events.html
   ;; You can use FOCUS in Capture phase until implementations converge.
   :focusin goog.events.EventType.FOCUSIN
   :focusout goog.events.EventType.FOCUSOUT

   ;; Forms
   :change goog.events.EventType.CHANGE
   :reset goog.events.EventType.RESET
   :select goog.events.EventType.SELECT
   :submit goog.events.EventType.SUBMIT
   :input goog.events.EventType.INPUT
   :propertychange goog.events.EventType.PROPERTYCHANGE

   ;; Drag and drop
   :dragstart goog.events.EventType.DRAGSTART
   :drag goog.events.EventType.DRAG
   :dragenter goog.events.EventType.DRAGENTER
   :dragover goog.events.EventType.DRAGOVER
   :dragleave goog.events.EventType.DRAGLEAVE
   :drop goog.events.EventType.DROP
   :dragend goog.events.EventType.DRAGEND

   ;; Touch events
   ;; Note that other touch events exist, but we should follow the W3C list here.
   ;; http://www.w3.org/TR/touch-events/#list-of-touchevent-types
   :touchstart goog.events.EventType.TOUCHSTART
   :touchmove goog.events.EventType.TOUCHMOVE
   :touchend goog.events.EventType.TOUCHEND
   :touchcancel goog.events.EventType.TOUCHCANCEL

   ;; Misc
   :beforeunload goog.events.EventType.BEFOREUNLOAD
   :consolemessage goog.events.EventType.CONSOLEMESSAGE
   :contextmenu goog.events.EventType.CONTEXTMENU
   :domcontentloaded goog.events.EventType.DOMCONTENTLOADED
   :error goog.events.EventType.ERROR
   :help goog.events.EventType.HELP
   :load goog.events.EventType.LOAD
   :losecapture goog.events.EventType.LOSECAPTURE
   :orientationchange goog.events.EventType.ORIENTATIONCHANGE
   :readystatechange goog.events.EventType.READYSTATECHANGE
   :resize goog.events.EventType.RESIZE
   :scroll goog.events.EventType.SCROLL
   :unload goog.events.EventType.UNLOAD

   ;; HTML 5 History events
   ;; See http://www.w3.org/TR/html5/browsers.html#event-definitions-0
   :hashchange goog.events.EventType.HASHCHANGE
   :pagehide goog.events.EventType.PAGEHIDE
   :pageshow goog.events.EventType.PAGESHOW
   :popstate goog.events.EventType.POPSTATE

   ;; Copy and Paste
   ;; Support is limited. Make sure it works on your favorite browser
   ;; before using.
   ;; http://www.quirksmode.org/dom/events/cutcopypaste.html
   :copy goog.events.EventType.COPY
   :paste goog.events.EventType.PASTE
   :cut goog.events.EventType.CUT
   :beforecopy goog.events.EventType.BEFORECOPY
   :beforecut goog.events.EventType.BEFORECUT
   :beforepaste goog.events.EventType.BEFOREPASTE

   ;; HTML5 online/offline events.
   ;; http://www.w3.org/TR/offline-webapps/#related
   :online goog.events.EventType.ONLINE
   :offline goog.events.EventType.OFFLINE

   ;; HTML 5 worker events
   :message goog.events.EventType.MESSAGE
   :connect goog.events.EventType.CONNECT

   ;; CSS animation events.
   ;; /** @suppress {missingRequire} */
   :animationstart goog.events.EventType.ANIMATIONSTART
   ;; /** @suppress {missingRequire} */
   :animationend goog.events.EventType.ANIMATIONEND
   ;; /** @suppress {missingRequire} */
   :animationiteration goog.events.EventType.ANIMATIONITERATION

   ;; CSS transition events. Based on the browser support described at:
   ;; https://developer.mozilla.org/en/css/css_transitions#Browser_compatibility
   ;; /** @suppress {missingRequire} */
   :transitionend goog.events.EventType.TRANSITIONEND

   ;; W3C Pointer Events
   ;; http://www.w3.org/TR/pointerevents/
   :pointerdown goog.events.EventType.POINTERDOWN
   :pointerup goog.events.EventType.POINTERUP
   :pointercancel goog.events.EventType.POINTERCANCEL
   :pointermove goog.events.EventType.POINTERMOVE
   :pointerover goog.events.EventType.POINTEROVER
   :pointerout goog.events.EventType.POINTEROUT
   :pointerenter goog.events.EventType.POINTERENTER
   :pointerleave goog.events.EventType.POINTERLEAVE
   :gotpointercapture goog.events.EventType.GOTPOINTERCAPTURE
   :lostpointercapture goog.events.EventType.LOSTPOINTERCAPTURE

   ;; IE specific events.
   ;; See http://msdn.microsoft.com/en-us/library/ie/hh772103(v=vs.85).aspx
   ;; Note: these events will be supplanted in IE11.
   :msgesturechange goog.events.EventType.MSGESTURECHANGE
   :msgestureend goog.events.EventType.MSGESTUREEND
   :msgesturehold goog.events.EventType.MSGESTUREHOLD
   :msgesturestart goog.events.EventType.MSGESTURESTART
   :msgesturetap goog.events.EventType.MSGESTURETAP
   :msgotpointercapture goog.events.EventType.MSGOTPOINTERCAPTURE
   :msinertiastart goog.events.EventType.MSINERTIASTART
   :mslostpointercapture goog.events.EventType.MSLOSTPOINTERCAPTURE
   :mspointercancel goog.events.EventType.MSPOINTERCANCEL
   :mspointerdown goog.events.EventType.MSPOINTERDOWN
   :mspointerenter goog.events.EventType.MSPOINTERENTER
   :mspointerhover goog.events.EventType.MSPOINTERHOVER
   :mspointerleave goog.events.EventType.MSPOINTERLEAVE
   :mspointermove goog.events.EventType.MSPOINTERMOVE
   :mspointerout goog.events.EventType.MSPOINTEROUT
   :mspointerover goog.events.EventType.MSPOINTEROVER
   :mspointerup goog.events.EventType.MSPOINTERUP

   ;; Native IMEs/input tools events.
   :text goog.events.EventType.TEXT
   :textinput goog.events.EventType.TEXTINPUT
   :compositionstart goog.events.EventType.COMPOSITIONSTART
   :compositionupdate goog.events.EventType.COMPOSITIONUPDATE
   :compositionend goog.events.EventType.COMPOSITIONEND

   ;; Webview tag events
   ;; See http://developer.chrome.com/dev/apps/webview_tag.html
   :exit goog.events.EventType.EXIT
   :loadabort goog.events.EventType.LOADABORT
   :loadcommit goog.events.EventType.LOADCOMMIT
   :loadredirect goog.events.EventType.LOADREDIRECT
   :loadstart goog.events.EventType.LOADSTART
   :loadstop goog.events.EventType.LOADSTOP
   :responsive goog.events.EventType.RESPONSIVE
   :sizechanged goog.events.EventType.SIZECHANGED
   :unresponsive goog.events.EventType.UNRESPONSIVE

   ;; HTML5 Page Visibility API.  See details at
   ;; {@code goog.labs.dom.PageVisibilityMonitor}.
   :visibilitychange goog.events.EventType.VISIBILITYCHANGE

   ;; LocalStorage event.
   :storage goog.events.EventType.STORAGE

   ;; DOM Level 2 mutation events (deprecated).
   :domsubtreemodified goog.events.EventType.DOMSUBTREEMODIFIED
   :domnodeinserted goog.events.EventType.DOMNODEINSERTED
   :domnoderemoved goog.events.EventType.DOMNODEREMOVED
   :domnoderemovedfromdocument goog.events.EventType.DOMNODEREMOVEDFROMDOCUMENT
   :domnodeinsertedintodocument goog.events.EventType.DOMNODEINSERTEDINTODOCUMENT
   :domattrmodified goog.events.EventType.DOMATTRMODIFIED
   :domcharacterdatamodified goog.events.EventType.DOMCHARACTERDATAMODIFIED

   ;; Print events.
   :beforeprint goog.events.EventType.BEFOREPRINT
   :afterprint goog.events.EventType.AFTERPRINT})

(defn listen [event-source event-keyword callback]
  (goog.events/listen event-source (keyword->event-type event-keyword) callback))
