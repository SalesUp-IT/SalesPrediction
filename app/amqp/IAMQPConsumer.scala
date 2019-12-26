package amqp


import com.rabbitmq.client.{AMQP, Consumer, Envelope, ShutdownSignalException}
import play.api.libs.json.Json
import play.api.{Configuration, Logger}

import scala.util.{Failure, Success, Try}

abstract class IAMQPConsumer[T](config: Configuration, queueName: String) extends IAMQPClient[T](config, queueName) with Consumer {

  private lazy val logger = Logger.logger

  def onConsume(o: T): Any

  private def unMarshalling(body: Array[Byte]): Option[T] = {
    Try {
      val source = new String(body, coddingPage)
      format.reads(Json.parse(source)).asOpt
    } match {
      case Success(result) =>
        result
      case Failure(_: Throwable) =>
        logger.error(s"Error of un-marshallig: ${new String(body, coddingPage)}")
        None
    }

  }

  override def handleCancel(consumerTag: String) {}

  override def handleCancelOk(consumerTag: String) {}

  override def handleRecoverOk(consumerTag: String) {}

  override def handleShutdownSignal(consumerTag: String, arg1: ShutdownSignalException) {}

  override def handleConsumeOk(consumerTag: String) {
    logger.trace(s"Consumer [${queueName}] registered: ${consumerTag}")
  }

  override def handleDelivery(consumerTag: String,
                              envelope: Envelope,
                              properties: AMQP.BasicProperties,
                              body: Array[Byte]): Unit = {
    unMarshalling(body) match {
      case Some(o) =>
        Try {
          onConsume(o)
        } match {
          case Success(_) =>
          case Failure(t: Throwable) =>
            logger.error(s"Consumer [${queueName}] has met the problem:", t)
        }
      case None =>
        logger.info(s"Consumer [${queueName}`] has got wrong message: ${consumerTag}")
    }
  }

  channel.basicConsume(queueName, true, this)

}
