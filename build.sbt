val kuromoji = "com.atilika.kuromoji" % "kuromoji-unidic" % "latest.integration"
val specs2 = "org.specs2" %% "specs2-core" % "4.2.0" % "test"
val kanatools = "com.mariten" % "kanatools" % "1.3.0"
//val argonaut = "io.argonaut" %% "argonaut" % "6.0.4"

lazy val commonSettings = Seq(
  organization := "com.stephenmac7",
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.12.6",
  publishMavenStyle := true
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "jcore",
    libraryDependencies ++= Seq(kuromoji, specs2, kanatools),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-Yrangepos"
    )
  )
