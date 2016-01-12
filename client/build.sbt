lazy val root = Project("bepo-client", file("."))

organization := "com.mentatlabs.bepo"
name := "bepo-client"
version := "0.0.2"

libraryDependencies ++= Seq(
  "com.dslplatform" % "dsl-client-java" % "1.4.1"
, "org.slf4j"       % "slf4j-api"       % "1.7.13"
)

assemblyJarName in assembly := name.value + "_" + scalaBinaryVersion.value + "-" + version.value + ".jar"
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
assemblyExcludedJars in assembly := (fullClasspath in assembly).value filterNot { cp =>
  cp.data.getName matches """bepo-client-model-\d{8}-\d{6}\.jar"""
}

/* Publish command: sbt clean package assembly publish */
publishTo := Some("Element Releases" at "http://repo.element.hr/nexus/content/repositories/releases/")
credentials ++= {
  val creds = Path.userHome / ".config" / "bepo" / "nexus.config"
  if (creds.exists) Some(Credentials(creds)) else None
}.toSeq
