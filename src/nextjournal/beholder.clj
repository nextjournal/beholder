(ns nextjournal.beholder
  (:import [io.methvin.watcher DirectoryChangeEvent DirectoryChangeEvent$EventType
            DirectoryChangeListener DirectoryWatcher]
           [java.nio.file Paths]))

(defn- fn->listener ^DirectoryChangeListener [f]
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

(defn- create
  "Creates a watcher taking a callback function `cb` that will be invoked
  whenever a file in one of the `paths` changes.

  Not meant to be called directly but use `watch` or `watch-blocking` instead."
  [cb paths]
  (-> (DirectoryWatcher/builder)
      (.paths (map to-path paths))
      (.listener (fn->listener cb))
      (.build)))

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
