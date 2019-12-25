name := "SalesPrediction"

version := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.10"

val commonMathVersion = "3.6.1"
val qpidVersion = "6.1.6"

val productionDependencies = Seq(
  guice,
  "commons-codec" % "commons-codec" % "1.9",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0",
  "org.apache.commons" % "commons-math3" % commonMathVersion,
  "org.apache.commons" % "commons-email" % "1.5",
  "com.rabbitmq" % "amqp-client" % "5.1.2",
  "net.debasishg" %% "redisclient" % "3.8",
  "org.quartz-scheduler" % "quartz" % "2.2.1",
  "io.swagger" %% "swagger-play2" % "1.6.0",
  "org.webjars" % "swagger-ui" % "3.2.2"
)

val testDependencies = Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test,
  "org.specs2" %% "specs2-mock" % "4.0.1" % Test,
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "2.2.0" % Test,
  "com.github.kstyrc" % "embedded-redis" % "0.6" % Test,
  "org.apache.qpid" % "qpid-broker" % qpidVersion % Test
)

libraryDependencies ++= productionDependencies ++ testDependencies

resolvers ++= Seq(
  "mvnrepository" at "http://mvnrepository.com/artifact/",
  "Sonatype" at "https://oss.sonatype.org/content/datasource.repositories/releases",
  "typesafe" at "http://repo.typesafe.com/typesafe/ivy-releases/",
  "typesafe-ivy-repo" at "http://typesafe.artifactoryonline.com/typesafe/releases",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "elasticsearch-releases" at "https://artifacts.elastic.co/maven"
)

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  // disable fatal warnings for the moment because of https://github.com/playframework/playframework/issues/7382
  // "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint", // Enable recommended additional warnings.
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-language:postfixOps", //Enable postfix operations
  "-language:implicitConversions", // Enable implicit conversions
  "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Ywarn-unused:-imports,_" // Play generates a routes with unused imports
)