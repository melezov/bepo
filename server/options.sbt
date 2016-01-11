scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-deprecation"
, "-encoding", "UTF-8"
, "-feature"
, "-language:_"
, "-target:jvm-1.8"
, "-unchecked"
, "-Xlint"
, "-Yclosure-elim"
, "-Yconst-opt"
, "-Ydead-code"
, "-Ywarn-adapted-args"
, "-Ywarn-dead-code"
, "-Ywarn-inaccessible"
, "-Ywarn-infer-any"
, "-Ywarn-nullary-override"
, "-Ywarn-nullary-unit"
, "-Ywarn-numeric-widen"
, "-Ywarn-unused"
)

wartremoverWarnings in (Compile, compile) ++= Seq(
  Wart.Product
, Wart.Serializable
, Wart.TryPartial
)

wartremoverErrors in (Compile, compile) ++= Seq(
  Wart.Any2StringAdd
, Wart.EitherProjectionPartial
, Wart.Enumeration
, Wart.JavaConversions
, Wart.Option2Iterable
, Wart.Return
)
