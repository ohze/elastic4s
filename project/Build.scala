import com.jsuereth.sbtpgp.SbtPgp
import com.jsuereth.sbtpgp.PgpKeys
import sbt._
import sbt.plugins.JvmPlugin
import sbt.Keys._
import sbtrelease.ReleasePlugin

object Build extends AutoPlugin {

  override def trigger  = AllRequirements
  override def requires = ReleasePlugin

  object autoImport {
    val org                    = "com.sksamuel.elastic4s"
    val AkkaVersion            = "2.6.1"
    val CatsVersion            = "2.1.0"
    val CirceVersion           = "0.12.3"
    val CommonsIoVersion       = "2.6"
    val ElasticsearchVersion   = "6.1.4"
    val ExtsVersion            = "1.61.1"
    val JacksonVersion         = "2.10.1"
    val JodaTimeVersion        = "2.10.5"
    val Json4sVersion          = "3.6.7"
    val SprayJsonVersion       = "1.3.5"
    val SttpVersion            = "1.7.2"
    val AWSJavaSdkVersion      = "1.11.699"
    val Log4jVersion           = "2.13.0"
    val LuceneVersion          = "7.1.0"
    val MockitoVersion         = "1.9.5"
    val PlayJsonVersion        = "2.8.1"
    val ReactiveStreamsVersion = "1.0.3"
    val ScalatestVersion       = "3.0.8"
    val Slf4jVersion           = "1.7.30"
  }

  import autoImport._

  override def projectSettings = Seq(
    organization := org,
    scalaVersion := "2.13.1",
    crossScalaVersions := Seq("2.12.10", "2.13.1"),
    publishMavenStyle := true,
    resolvers += Resolver.mavenLocal,
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
    Test / publishArtifact := false,
    fork := false,
    ThisBuild / parallelExecution in ThisBuild := false,
    SbtPgp.autoImport.useGpg := true,
    SbtPgp.autoImport.useGpgAgent := true,
    Global / concurrentRestrictions += Tags.limit(Tags.Test, 1),
    sbtrelease.ReleasePlugin.autoImport.releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    sbtrelease.ReleasePlugin.autoImport.releaseCrossBuild := true,
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    javacOptions := Seq("-source", "1.7", "-target", "1.7"),
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.3",
      "com.sksamuel.exts" %% "exts"       % ExtsVersion,
      "org.typelevel"     %% "cats-core"  % CatsVersion,
      "org.slf4j"         % "slf4j-api"   % Slf4jVersion,
      "org.mockito"       % "mockito-all" % MockitoVersion % "test",
      "org.scalatest"     %% "scalatest"  % ScalatestVersion % "test"
    ),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (version.value.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := {
      <url>https://github.com/sksamuel/elastic4s</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:sksamuel/elastic4s.git</url>
          <connection>scm:git@github.com:sksamuel/elastic4s.git</connection>
        </scm>
        <developers>
          <developer>
            <id>sksamuel</id>
            <name>sksamuel</name>
            <url>http://github.com/sksamuel</url>
          </developer>
        </developers>
    }
  )
}
