import javax.inject._
import play.api.Logger
import play.api.inject.ApplicationLifecycle
import quartz.QuartzSchedulerProvider

import scala.concurrent.Future
import scala.util.Try

@Singleton
class ApplicationHooks @Inject()(lifecycle: ApplicationLifecycle,
                                 quartz: QuartzSchedulerProvider) {

  import scala.concurrent.ExecutionContext.Implicits.global

  private lazy val logger = Logger.logger

  lifecycle.addStopHook(
    () => Future {
      logger.info(s"Application stop hook is started")
      Try {
        quartz.shutdown()
      }
    }
  )
}
