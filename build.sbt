import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.marimon"
ThisBuild / organizationName := "marimon"

lazy val root = (project in file("."))
  .settings(
    name := "parboiled2-experiments",
    libraryDependencies += "org.parboiled" %% "parboiled" % "2.4.0",
    libraryDependencies += scalaTest % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
