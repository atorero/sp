name := """scienceprovider"""

version := "0.4-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

//resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"

resolvers += "Internal artifactory" at "https://artprod.dev.bloomberg.com/artifactory/root-repos"

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.195"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"

libraryDependencies += "com.mohiva" %% "play-silhouette" % "5.0.0"

libraryDependencies += "org.webjars" % "jquery" % "3.2.1"
