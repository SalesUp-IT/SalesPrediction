package servers

import concurrency.ConcurrentFixture
import org.scalatest.BeforeAndAfterAll
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

abstract class PlaySpecWithEmbeddedServers(servers: IEmbeddedServer*)
  extends PlaySpec
    with GuiceOneAppPerSuite
    with ScalaFutures
    with ConcurrentFixture
    with BeforeAndAfterAll {

  override def fakeApplication: Application = {

    servers.foreach {
      server =>
        server.start()
    }

    val fakeAppConfig: Seq[(String, Any)] = (for {
      server <- servers
      conf <- server.configs
    } yield {
      conf._1 -> conf._2
    }) ++ Seq(
      "app.productionScope" -> false,
      "app.timeout.distributed.lock" -> "1 min"
    )

    val result = new GuiceApplicationBuilder()
      .configure(
        fakeAppConfig: _*
      ).build()

    result
  }


  override def afterAll(): Unit = {
    servers.reverse.foreach {
      server =>
        server.shutdown()
    }
  }


}
