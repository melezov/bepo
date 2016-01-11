lazy val root = Project("bepo-client", file("."))

organization := "com.mentatlabs.bepo"
name := "bepo-client"
version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.dslplatform" % "dsl-client-java" % "1.4.1"
, "ch.qos.logback"  % "logback-classic" % "1.1.3"
)
