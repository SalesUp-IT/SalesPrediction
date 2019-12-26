package datasource.mongodb

import com.google.inject.Provider
import io.lemonlabs.uri.Url
import javax.inject.Inject
import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.api.Configuration
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import scala.util.{Failure, Success, Try}

@javax.inject.Singleton
class MongoDatabaseProvider @Inject()(configuration: Configuration) extends Provider[MongoDatabase] {

  private[this] lazy val mongoUri: String = configuration
    .getOptional[String]("mongodb.uri")
    .getOrElse("mongodb://localhost:27017/naomi")

  override def get(): MongoDatabase = {
    Url.parseTry(mongoUri) match {
      case Success(url) =>
        val client = MongoClient(mongoUri)
        val dbName = Try {
          url.path.parts(0)
        }.getOrElse("sales_predictor")

        val codecRegistry = fromRegistries(fromProviders(
          //classOf[UserSession]
        ), DEFAULT_CODEC_REGISTRY)

        client.getDatabase(dbName).withCodecRegistry(codecRegistry)

      case Failure(_: Throwable) =>
        throw new Exception(s"Cannot parse url for MongoDB client")
    }
  }

}
