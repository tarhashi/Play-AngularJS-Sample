import play.Project._
import sbt._
import Keys._

object ApplicationBuild extends Build {

    val appName         = "play-todo"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
        jdbc,
        "org.squeryl" %% "squeryl" % "0.9.5-6"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
        libraryDependencies ++= Seq(
           "org.squeryl" %% "squeryl" % "0.9.5-6"
        )
    )

}
