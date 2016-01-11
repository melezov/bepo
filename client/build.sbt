lazy val root = Project("bepo-client", file("."))

organization := "com.mentatlabs.bepo"
name := "bepo-client"
version := "0.0.1"

libraryDependencies ++= Seq(
  "com.dslplatform" % "dsl-client-java" % "1.4.1"
, "org.slf4j"       % "slf4j-api"       % "1.7.13"
)

publishTo := Some("Element Releases" at "http://repo.element.hr/nexus/content/repositories/releases/")
credentials ++= {
  val creds = Path.userHome / ".config" / "bepo" / "nexus.config"
  if (creds.exists) Some(Credentials(creds)) else None
}.toSeq
