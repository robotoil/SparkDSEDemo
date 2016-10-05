import com.datastax.spark.connector.toSparkContextFunctions
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object SimpleScalaApp {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Scala Application")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    // Simple calculation on the cluster.
    /*
    val rdd = sc.parallelize(List(1, 2, 3)).map(i => i + 1)
    rdd.collect().foreach(println)
    */


    // Spark SQL Example going against DSE.

    // Register the Cassandra table with Spark.
    val df3 = sqlContext.read.format("org.apache.spark.sql.cassandra")
      .options(Map("keyspace" -> "sparkdemo", "table" -> "names"))
      .load()
      .registerTempTable("demoNames")

    // SQL Query
    val df4 = sqlContext.sql("select * from demoNames where firstname = 'Eric'")
    df4.collect().foreach(println)
  }
}