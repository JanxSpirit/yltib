name := "yltib"

organization := "com.janxspirit"

version := "1.0"

scalaVersion := "2.9.1"

publishTo := Some("Wordnik CI Server" at "https://ci.aws.wordnik.com/artifactory/libs-rel")

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.0" % "compile",
  "org.specs2" % "specs2_2.9.1" % "1.8.2" % "test",
  "junit" % "junit" % "4.7" % "test")