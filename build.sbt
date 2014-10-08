name := "OurFencingClub"

version := "1.0-SNAPSHOT"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.stackmob" %% "newman" % "1.3.5",
  "org.kapunga" %% "fredconnect" % "0.3.1-SNAPSHOT"
)

play.Project.playScalaSettings
