lazy val root = Project("bepo-server", file("."))

organization := "com.mentatlabs.bepo"
name := "bepo-server"
version := "0.0.1-SNAPSHOT"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.revenj" % "revenj-servlet" % "0.8.1" exclude("hr.ngs.templater", "templater")
, "com.sun.mail" % "javax.mail" % "1.5.5"

, "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided"

, "com.typesafe.scala-logging" %% "scala-logging"  % "3.1.0"
, "ch.qos.logback"             % "logback-classic" % "1.1.3"
)

containerPort := 9090
containerLibs in Jetty := Seq("org.eclipse.jetty" % "jetty-runner" % "9.3.6.v20151106" intransitive())
enablePlugins(JettyPlugin)
