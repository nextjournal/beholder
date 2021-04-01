# Beholder

[![Clojars Project](https://img.shields.io/clojars/v/com.nextjournal/beholder.svg)](https://clojars.org/com.nextjournal/beholder)

The Clojure directory watcher from
[krell](https://github.com/vouch-opensource/krell/) as a standalone
library.

![Beholder Logo](https://cdn.nextjournal.com/data/QmWMRZdwLqn9Ynt8JAxvNF9eWi3HF4c3UcT9vSXVSBS7Wi?filename=beholder.jpg&content-type=image/jpeg)

Built using the Java library
[directory-watcher](https://github.com/gmethvin/directory-watcher).
From its README:

> A directory watcher utility for JDK 8+ that aims to provide accurate
> and efficient recursive watching for Linux, macOS and Windows. In
> particular, this library provides a JNA-based WatchService for Mac
> OS X to replace the default polling-based JDK implementation.
>
> The core directory-watcher library is designed to have minimal
> dependencies; currently it only depends on slf4j-api (for internal
> logging, which can be disabled by passing a NOPLogger in the
> builder) and jna (for the macOS watcher implementation).

Developed by [David Nolen](https://github.com/swannodette).

## Usage
```clojure
(require '[nextjournal.beholder :as beholder]
(def beholder
  (doto (beholder/create prn "src")
    beholder/watch))

(beholder/stop watcher)
```
