# directory-watcher
A fast directory watcher in Clojure using
[directory-watcher](https://github.com/gmethvin/directory-watcher). From
its README:

> A directory watcher utility for JDK 8+ that aims to provide accurate
> and efficient recursive watching for Linux, macOS and Windows. In
> particular, this library provides a JNA-based WatchService for Mac
> OS X to replace the default polling-based JDK implementation.
>
> The core directory-watcher library is designed to have minimal
> dependencies; currently it only depends on slf4j-api (for internal
> logging, which can be disabled by passing a NOPLogger in the
> builder) and jna (for the macOS watcher implementation).

Developed by [David Nolen](https://github.com/swannodette) & extracted
from
[krell](https://github.com/vouch-opensource/krell/blob/2923683445c30f803d7bcc5f7f862686c90c2612/src/krell/watcher.clj).


## Usage
```clojure
(require '[nextjournal.directory-watcher :as dw]
(def watcher
  (doto (dw/create prn "src")
    dw/watch))

(dw/stop watcher)
```
