enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
dockerExposedPorts := Seq(9090)
dockerRepository := Some("mentatlabs")
dockerUpdateLatest := true
packageName in Docker := "bepo-server"
javaOptions in Universal ++= Seq("-J-Xmx1024m", "-J-Xss4m")
