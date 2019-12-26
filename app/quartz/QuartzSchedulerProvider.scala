package quartz

import javax.inject.Provider
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
import play.api.Logger

import scala.util.{Failure, Success, Try}

@javax.inject.Singleton
class QuartzSchedulerProvider extends Provider[Scheduler] {

  private lazy val scheduler = StdSchedulerFactory.getDefaultScheduler

  private lazy val logger = Logger.logger

  logger.info(s"Scheduler will be stopped")
  Try {
    scheduler.start()
  } match {
    case Success(_) =>
      logger.info(s"Scheduler has been started")
    case Failure(t) =>
      logger.error(s"Scheduler has no been started", t)
  }

  def shutdown(): Unit = {
    if (scheduler.isStarted) {
      logger.info(s"Scheduler will be stopped")
      scheduler.shutdown(true)
      logger.info(s"Scheduler has been stopped")
    }
  }

  override def get(): Scheduler = {
    if (scheduler.isStarted) {
      scheduler
    } else {
      throw new Exception(s"Scheduler has no been started")
    }
  }
}
