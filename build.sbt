name := """Accio"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"
val Json4sVersion = "3.4.0"

libraryDependencies ++= Seq(
  ws,
  "org.json4s" %% "json4s-jackson" % Json4sVersion,
  "org.json4s" %% "json4s-ext" % Json4sVersion,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


fork in run := true
