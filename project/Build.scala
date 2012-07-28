import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play-todo"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
        "org.squeryl" %% "squeryl" % "0.9.5-2"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
        // lift-json-play-module
        resolvers += "tototoshi.github.com maven-repo/releases" at "http://tototoshi.github.com/maven-repo/releases",
        libraryDependencies ++= Seq(
           "com.github.tototoshi" %% "lift-json-play-module" % "0.1", 
           "org.squeryl" %% "squeryl" % "0.9.5-2"
        )
    )

}
