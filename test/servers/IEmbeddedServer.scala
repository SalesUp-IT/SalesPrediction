package servers


import java.net.ServerSocket

import play.api.Logger

import scala.util.{Failure, Success, Try}

trait IEmbeddedServer extends IServer {

  final val host: String = "localhost"

  private lazy val logger = Logger.logger

  def serverName: String

  lazy val port: Int = Try {
    val socket = new ServerSocket(0)
    val freePort = socket.getLocalPort
    if (null != socket) {
      socket.close()
    }
    freePort
  } match {
    case Success(_port) =>
      _port
    case Failure(t: Throwable) =>
      logger.error(s"Cannot allocate port for server: $serverName", t)
      throw new RuntimeException(s"Cannot allocate port for server: $serverName")
  }

  def configs: Map[String, Any]

  override def start(): Unit = {
    logger.info(s"Start server: name = ${serverName}, conf = ${configs.mkString(", ")}")
  }

  override def shutdown(): Unit = {
    logger.info(s"Stop server: name = ${serverName}, conf = ${configs.mkString(", ")}")
  }

}
