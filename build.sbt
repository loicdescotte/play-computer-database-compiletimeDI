name := "computer-database-scala"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  jdbc,
  ws,
  evolutions,
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "org.webjars" % "jquery" % "2.1.4",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalatestplus" %% "play" % "1.4.0-M4" % "test"
)     

lazy val root = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator