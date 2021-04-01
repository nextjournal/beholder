(ns nextjournal.beholder
  (:import [io.methvin.watcher DirectoryChangeEvent DirectoryChangeEvent$EventType
            DirectoryChangeListener DirectoryWatcher]
           [java.nio.file Paths]))

(defn fn->listener ^DirectoryChangeListener [f]
  (reify
    DirectoryChangeListener
    (onEvent [this e]
      (let [path (.path ^DirectoryChangeEvent e)]
        (condp = (. ^DirectoryChangeEvent e eventType)
          DirectoryChangeEvent$EventType/CREATE   (f {:type :create :path path})
          DirectoryChangeEvent$EventType/MODIFY   (f {:type :modify :path path})
          DirectoryChangeEvent$EventType/DELETE   (f {:type :delete :path path})
          DirectoryChangeEvent$EventType/OVERFLOW (f {:type :overflow :path path}))))))

(defn to-path [& args]
  (Paths/get ^String (first args) (into-array String (rest args))))

(defn create [cb paths]
  (-> (DirectoryWatcher/builder)
      (.paths (map to-path paths))
      (.listener (fn->listener cb))
      (.build)))

(defn watch [cb & paths]
  (doto (create cb paths)
    (.watchAsync)))

(defn watch-blocking [cb & paths]
  (doto (create cb paths)
    (.watch)))

(defn stop [^DirectoryWatcher watcher]
  (.close watcher))

(comment
  (def watcher
    (watch prn "src"))

  (.watchAsync watcher)

  (stop watcher)

  )
