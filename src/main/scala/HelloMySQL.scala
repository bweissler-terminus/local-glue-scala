import com.amazonaws.services.glue.GlueContext
import com.amazonaws.services.glue.log.GlueLogger
import com.amazonaws.services.glue.util.{GlueArgParser, JsonOptions}
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession

object HelloMySQL {

  def main(args: Array[String]): Unit = {

    // to properly use this in aws glue, add a log4j.properties file to the s3 location referenced by
    // the --extra-files special parameter on the glue job
    val logger = new GlueLogger

    // start simulation
    val options = GlueArgParser.getResolvedOptions(
      args,
      Array("show")
    )

    val configFactory = ConfigFactory.load()
    val config = configFactory.getConfig("org.example.cdp-audits")
    val host = config.getConfig("mysql").getString("url")
    val user = config.getConfig("mysql").getString("username")
    val pass = config.getConfig("mysql").getString("password")

    implicit val spark: SparkSession = SparkSession
      .builder()
      .getOrCreate()

    val glue = new GlueContext(spark.sparkContext)

    logger.info(s"System properties says, running ${System.getProperty("spark.app.name")} on ${System.getProperty("spark.master")}")
    logger.info(s"Glue says, running ${glue.sparkContext.appName} on ${glue.sparkContext.master}")

    glue
      .getSource(
        connectionType = "mysql",
        connectionOptions = JsonOptions(
          Map(
            "url"      -> host,
            "dbtable"  -> "baz",
            "user"     -> user,
            "password" -> pass,
            "useSSL"   -> "false"
          )
        )
      )
      .getDynamicFrame()
      .toDF()
      .show()
  }
}
