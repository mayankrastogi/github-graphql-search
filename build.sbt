name := "mayank_k_rastogi_cs474_hw1"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  // Typesafe Configuration Library
  "com.typesafe" % "config" % "1.3.4",

  // Logback logging framework
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.gnieh" % "logback-config" % "0.4.0",

  // ScalaJ Http Client
  "org.scalaj" %% "scalaj-http" % "2.4.2",

  // JSON (De)Serialization Libraries
  "com.google.code.gson" % "gson" % "2.8.5",
  "net.liftweb" %% "lift-json" % "3.3.0",

  // Scalatest testing framework
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)
