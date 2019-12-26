package servers.amqp

import java.io.{File, FileWriter}

import com.google.common.io.Files
import org.apache.qpid.server.{Broker, BrokerOptions}
import resources.IResourceReader
import servers.IEmbeddedServer

object EmbeddedAmqpBroker extends IEmbeddedServer with IResourceReader {

  private[this] var broker: Broker = _

  private[this] lazy val tempDir: File = Files.createTempDir()
  private[this] lazy val homeDir: File = Files.createTempDir()

  private[this] val secret: String = "guest:guest"

  private[this] def preparePasswordFile: Unit = {
    val ectDir = new File(homeDir, "etc")
    ectDir.mkdir()
    if (ectDir.exists()) {
      val passwordField = new File(ectDir, "passwd")
      val writer = new FileWriter(passwordField)
      writer.write(secret)
      writer.flush()
      writer.close()
    } else {
      throw new Exception(s"Password file cannot be created")
    }
  }

  override def serverName: String = "Embedded AMQP Broker"

  override def start(): Unit = {

    super.start()

    broker = new Broker()
    val brokerOptions = new BrokerOptions
    preparePasswordFile
    brokerOptions.setConfigProperty("qpid.home_dir", homeDir.getAbsolutePath)
    brokerOptions.setConfigProperty("qpid.work_dir", tempDir.getAbsolutePath)
    brokerOptions.setConfigProperty("qpid.amqp_port", port.toString)
    brokerOptions.setInitialConfigurationLocation(getResourceInExternalForm("qpid-embedded-configuration.json"))
    broker.startup(brokerOptions)
  }

  override def shutdown(): Unit = {
    super.shutdown()
    if (null != broker) {
      broker.shutdown()
      tempDir.delete()
      homeDir.delete()
    }
  }

  override def configs: Map[String, Any] = Map(
    "amqp.uri" -> s"amqp://$secret@localhost:$port"
  )
}
