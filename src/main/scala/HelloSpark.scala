import com.amazonaws.services.glue.GlueContext
import com.amazonaws.services.glue.log.GlueLogger
import com.amazonaws.services.glue.util.GlueArgParser
import org.apache.log4j.LogManager
import org.apache.spark.sql.SparkSession

object HelloSpark {

  def main(args: Array[String]): Unit = {

    val logger = new GlueLogger

    // start simulation
    val options = GlueArgParser.getResolvedOptions(
      args,
      Array("show")
    )

    implicit val spark: SparkSession = SparkSession
      .builder()
      .getOrCreate()

    import spark.implicits._

    val glue = new GlueContext(spark.sparkContext)

    logger.info(s"System properties says, running ${System.getProperty("spark.app.name")} on ${System.getProperty("spark.master")}")
    logger.info(s"Glue says, running ${glue.sparkContext.appName} on ${glue.sparkContext.master}")


    val characters = glue.sparkContext.parallelize(Seq(
      ("vince", "chase"),
      ("john", "drama"),
      ("turtle", null),
      ("eric", "murphy"),
      ("sloan", "mcquewick")
    ))

    val charactersDf = glue.createDataFrame(characters).toDF("firstName", "lastName")

    charactersDf
      .filter("lastName is not null")
      .withColumnRenamed("firstName", "first name")
      .withColumnRenamed("lastName", "last name")
      .show()
  }
}
