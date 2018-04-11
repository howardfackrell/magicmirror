name := """magicmirror"""
organization := "com.hlf"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.hlf.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.hlf.binders._"

val user = sys.env.getOrElse("OCT_VAULT_SHARED_READ_ARTIFACTORY_USERNAME", "invalid artifactory user")
val token = sys.env.getOrElse("OCT_VAULT_SHARED_READ_ARTIFACTORY_PASSWORD", "invalid artifactory password")
credentials += Credentials("Artifactory Realm", "artifactory.octanner.net", user, token)

updateConfiguration in updateSbtClassifiers := (updateConfiguration in updateSbtClassifiers).value.withMissingOk(true)
