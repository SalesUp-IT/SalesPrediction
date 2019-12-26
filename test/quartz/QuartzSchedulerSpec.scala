package quartz

import concurrency.ConcurrentFixture
import javax.inject.{Inject, Singleton}
import org.quartz._
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Configuration

object Semaphore {

  private[this] var consumed: Int = 0

  def inc(): Unit = {
    consumed = consumed + 1
  }

  def getValue: Int = consumed
}

@Singleton
class TestJob @Inject()(config: Configuration) extends Job {

  override def execute(context: JobExecutionContext): Unit = {
    Semaphore.inc()
  }

}


class QuartzSchedulerSpec extends PlaySpec
  with GuiceOneAppPerSuite
  with ScalaFutures
  with ConcurrentFixture {

  private val schedulerProvider = app.injector.instanceOf(classOf[QuartzSchedulerProvider])
  private val jobFactory = app.injector.instanceOf(classOf[GuiceJobFactory])

  "Quartz Scheduler" should {

    "execute job" in {

      Semaphore.getValue mustBe 0

      val scheduler = schedulerProvider.get()
      scheduler.setJobFactory(jobFactory)

      val job = JobBuilder.newJob(classOf[TestJob])
        .withIdentity("TestJob", "Group")
        .build

      val trigger = TriggerBuilder
        .newTrigger()
        .withIdentity("TestJobTrigger", "Group")
        .startNow().build()

      scheduler.scheduleJob(job, trigger)

      EventuallyLong {
        Semaphore.getValue mustBe 1

      }

    }

  }
}
