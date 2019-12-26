package controllers.fixtures


import org.apache.commons.codec.binary.Base64
import org.mongodb.scala.bson.ObjectId
import play.api.http.Writeable
import play.api.libs.json.Reads
import play.api.mvc._
import play.mvc.Http.HeaderNames

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class CORSController()(implicit cc: ControllerComponents)
  extends AbstractController(cc)
    with Results {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit def strToObjectId(str: String): ObjectId = {
    Try {
      new ObjectId(str)
    }.getOrElse(new ObjectId)
  }

  def authenticatedOK[C](content: C)(implicit writeable: Writeable[C]): Result = {
    Ok(content).withHeaders(
      HeaderNames.ALLOW -> "*",
      HeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN -> "*"
    )
  }


  def unauthorized: Result = Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured"""")

  def splitEncodedToken(token: String): List[String] = new String(Base64.decodeBase64(token.getBytes)).split(":").toList

  final val authTag = "Authorization"


  def withAuthorizationToken(token: String => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
    request.headers.get("Authorization")
      .orElse(request.headers.get("Csrf-Token")) match {
      case Some(authorization) =>
        authorization.split(" ").drop(1).headOption match {
          case Some(_token) =>
            token.apply(_token)

          case None =>
            Future {
              unauthorized
            }
        }

      case None =>
        Future {
          unauthorized
        }
    }
  }

  def withBody[R](block: R => Future[Result])(implicit r: Reads[R], request: Request[AnyContent]): Future[Result] = {
    request.body.asJson match {
      case Some(json) =>
        Try {
          json.as[R]
        } match {
          case Success(o) =>
            block.apply(o)
          case Failure(e) =>
            Future(BadRequest(e.getMessage))
        }
      case None => Future {
        BadRequest("No body")
      }
    }
  }

}
