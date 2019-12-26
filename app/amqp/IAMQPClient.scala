package amqp

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}
import play.api.Configuration
import play.api.libs.json.Format


abstract class IAMQPClient[T](config: Configuration, queueName: String) {

  private val uri: String = config.getOptional[String](s"amqp.uri").getOrElse(s"amqp://guest:guest@localhost:5672")

  final val coddingPage = "UTF-8"

  def format: Format[T]

  lazy val connection: Connection = {
    val factory = new ConnectionFactory()
    factory.setUri(uri)
    factory.newConnection()
  }

  lazy val channel: Channel = {
    val _channel = connection.createChannel()
    _channel.queueDeclare(queueName, true, false, false, null)
    _channel.exchangeDeclare(queueName, "direct", true)
    _channel.queueBind(queueName, queueName, queueName)
    _channel
  }


}
