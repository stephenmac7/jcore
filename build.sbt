val kuromoji = "com.atilika.kuromoji" % "kuromoji-unidic" % "latest.integration"
val specs2 = "org.specs2" %% "specs2-core" % "3.7" % Test
//val argonaut = "io.argonaut" %% "argonaut" % "6.0.4"

lazy val commonSettings = Seq(
  organization := "com.stephenmac7",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.7",
  publishMavenStyle := true
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "jcore",
    libraryDependencies ++= Seq(kuromoji, specs2),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-Yrangepos"
    )
  )
