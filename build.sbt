name := "trueaccord-takehome"

version := "0.1"

scalaVersion := "2.12.11"

val circeVersion = "0.11.2"
lazy val mockitoTestVersion = "1.10.19"
lazy val finagleVersion = "6.45.0"
lazy val scalaTestVersion = "3.0.1"

libraryDependencies += "com.twitter" %% "finagle-http" % "21.3.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.mockito" % "mockito-all" % mockitoTestVersion % "test"
)
