import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Dependencies {
  // Library Versions
  val catsVersion = "2.10.0"
  val catsEffectVersion = "3.5.1"
  val fs2Version = "3.6.1"
  val parsleyVersion = "4.3.1"
  val parsleyCatsVersion = "1.2.0"

  val munitVersion = "0.7.29"

  // Libraries
  val catsEffect =
    Def.setting("org.typelevel" %%% "cats-effect" % catsEffectVersion)
  val catsCore = Def.setting("org.typelevel" %%% "cats-core" % catsVersion)
  val fs2Core = Def.setting("co.fs2" %% "fs2-core" % fs2Version)
  val parsley = "com.github.j-mie6" %% "parsley" % parsleyVersion
  val parsleyCats = "com.github.j-mie6" %% "parsley-cats" % parsleyCatsVersion

  val munit = Def.setting("org.scalameta" %% "munit" % munitVersion % "test")
}
