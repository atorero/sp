name := """scienceprovider"""

version := "0.4.3-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  "Atlassian Releases" at "https://maven.atlassian.com/public/",
  "Internal artifactory" at "https://artprod.dev.bloomberg.com/artifactory/root-repos"
)

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test,
  "com.h2database" % "h2" % "1.4.195",

  "com.typesafe.play" %% "play-slick" % "3.0.0",
  //"io.github.nafg" %% "slick-migration-api" % "0.4.2",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1",

  "com.mohiva" %% "play-silhouette" % "5.0.0",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.0",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.0",
  "com.iheart" %% "ficus" % "1.4.3",
  "net.codingwell" %% "scala-guice" % "4.1.0",

  "org.webjars" % "jquery" % "3.2.1",
  "org.webjars" % "jquery-validation" % "1.17.0"
)
