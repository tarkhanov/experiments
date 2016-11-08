name := "ExampleActor"

version := "1.0"

scalaVersion := "2.11.8"


val akkaVersion = "2.4.4"


libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-sharding" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-persistence" % akkaVersion
libraryDependencies += "org.iq80.leveldb" % "leveldb" % "0.7"
libraryDependencies += "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"


// libraryDependencies += "com.github.romix.akka" %% "akka-kryo-serialization" % "0.4.1"