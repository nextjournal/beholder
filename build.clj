(ns build
  (:require [clojure.tools.build.api :as b]
            [clojure.edn :as edn]))

(def project (edn/read-string (slurp "release.edn")))
(def lib (symbol (:group-id project) (:artifact-id project)))

(def version (:version project))
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))
(def jar-file (format "target/%s-%s.jar" (name lib) version))

(defn clean [_]
  (b/delete {:path "target"}))

(defn jar [_]
  (b/write-pom {:class-dir class-dir
                :lib lib
                :version version
                :basis basis
                :src-dirs ["src"]
                :scm (:scm project)
                :pom-data
                [[:description "The Clojure directory watcher from Krell as a standalone library"]
                 [:url (-> project :scm :url)]
                 [:licenses
                  [:license
                   [:name "EPL-1.0"]
                   [:url "https://www.eclipse.org/org/documents/epl-v10.html"]]]]})
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn install [_]
  (jar {})
  (b/install {:basis basis
              :lib lib
              :version version
              :jar-file jar-file
              :class-dir class-dir})
  (println "Installed beholder" (str "v" version)))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "resources"]
               :target-dir class-dir})
  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis basis}))

(defn deploy [opts]
  (jar opts)
  ((requiring-resolve 'deps-deploy.deps-deploy/deploy)
    (merge {:installer :remote
                       :artifact jar-file
                       :pom-file (b/pom-path {:lib lib :class-dir class-dir})}
                    opts))
  opts)

