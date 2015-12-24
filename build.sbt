name := "SW4"

version := "1.0"

scalaVersion := "2.11.7"

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "net.liftweb" %% "lift-json" % "2.6+"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")