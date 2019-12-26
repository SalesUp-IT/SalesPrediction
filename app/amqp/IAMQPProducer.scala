package amqp

import com.rabbitmq.client.MessageProperties
import play.api.Configuration

abstract class IAMQPProducer[T](config: Configuration, queueName: String) extends IAMQPClient[T](config, queueName) {

  def send(o: T): Unit = {

    val message = format.writes(o).toString()
    channel.basicPublish(queueName, queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(coddingPage))
  }

}
