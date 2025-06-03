(ns nextjournal.beholder
  (:require [babashka.pods :as pods]))

(pods/load-pod 'org.babashka/fswatcher "0.0.5")

(require '[pod.babashka.fswatcher :as fw])

(prn :todo)

(fw/watch "src" (fn [event]
                  (prn event))
          {:recursive true})
;;;;

(prn :done)

#_(defn- fn->listener ^DirectoryChangeListener [f]
  (reify
    DirectoryChangeListener
    (onEvent [this e]
      (let [path (.path ^DirectoryChangeEvent e)]
        (condp = (. ^DirectoryChangeEvent e eventType)
          DirectoryChangeEvent$EventType/CREATE   (f {:type :create :path path})
          DirectoryChangeEvent$EventType/MODIFY   (f {:type :modify :path path})
          DirectoryChangeEvent$EventType/DELETE   (f {:type :delete :path path})
          DirectoryChangeEvent$EventType/OVERFLOW (f {:type :overflow :path path}))))))

(defn- to-path [& args]
  (Paths/get ^String (first args) (into-array String (rest args))))

(defn watch
  "Creates a directory watcher that will invoke the callback function `cb` whenever
  a file event in one of the `paths` occurs. Watching will happen asynchronously.

  Returns a directory watcher that can be passed to `stop` to stop the watch."
  [cb & paths]
  (doto (create cb paths)
    (.watchAsync)))

(defn watch-blocking
  "Blocking version of `watch`."
  [cb & paths]
  (doto (create cb paths)
    (.watch)))

(defn stop
  "Stops the watch for a given `watcher`."
  [^DirectoryWatcher watcher]
  (.close watcher))

(comment
  ;; to start a watch with a callback function and paths to watch
  (def watcher
    (watch prn "src"))

  ;; stop the watch again using the return value from watch
  (stop watcher)

  )
