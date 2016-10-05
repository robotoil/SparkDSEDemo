// This was developed on DSE 5.0.2
import sbt.complete.Parsers
name := "SparkDSEDemo"
version := "1.0"
scalaVersion := "2.10.5"
val sparkVersion = "1.6.1"


libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
//libraryDependencies += "org.apache.spark" %% "spark-hive" % sparkVersion % "provided"
//libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided"

resolvers += "DataStax Repo" at "https://datastax.artifactoryonline.com/datastax/public-repos/"
libraryDependencies += "com.datastax.dse" % "dse-spark-dependencies" % "5.0.1" % "provided"

// Do not include Scala in the assembled JAR. DSE has it's own copy.
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

val dse_run = inputKey[Unit]("Builds the package and ships it to the DSE cluster with the specified class and params")
dse_run := {
  val args: Seq[String] = Parsers.spaceDelimited("<arg>").parsed

  if (args.nonEmpty) {
    val class_name = args.head
    val params = args.tail.mkString(" ")
    val command = "dse spark-submit  --class " + class_name + " " + assembly.value + " " + params
    //val command = "dse spark-submit --executor-memory 4G --class " + class_name + " " + assembly.value + " " + params

    System.err.println(command)

    command !
  } else {
    System.err.println("Missing class argument")
  }
}
