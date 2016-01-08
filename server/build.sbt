lazy val root = Project("bepo-server", file("."))

organization := "com.mentatlabs.bepo"
name := "bepo-server"
version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.revenj" % "revenj-servlet" % "0.8.0"
)
