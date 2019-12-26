package servers.amqp

import amqp.{IAMQPConsumer, IAMQPProducer}
import play.api.Configuration
import play.api.libs.json.{Format, Json}
import servers.PlaySpecWithEmbeddedServers

class AbstractAmqpSpec extends PlaySpecWithEmbeddedServers(EmbeddedAmqpBroker) {

  private val config = app.injector.instanceOf(classOf[Configuration])

  final val queueName = "queue.test"

  private var consumed: Boolean = false

  private val producer = new IAMQPProducer[TestMessage](config, queueName) {
    override def format: Format[TestMessage] = Json.format[TestMessage]
  }

  private val consumer = new IAMQPConsumer[TestMessage](config, queueName) {
    override def format: Format[TestMessage] = Json.format[TestMessage]

    override def onConsume(o: TestMessage): Any = {
      consumed = o.body == "test"
    }
  }

  "AbstractAmqpSpec" should {

    "send and get message" in {
      producer.send(TestMessage("test"))
      EventuallyLong {
        consumed mustBe true
      }

    }

  }

}
