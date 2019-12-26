package quartz


import java.util.Date

import entities.models._
import javax.inject._
import org.quartz._
import play.api.{Configuration, Logger}

import scala.util.Try

@Singleton
class QuartzJobExecutor @Inject()(schedulerProvider: QuartzSchedulerProvider, jobFactory: GuiceJobFactory, config: Configuration) {

  private def int2Integer(x: Int): Integer = java.lang.Integer.valueOf(x)

  private val productionScope: Boolean = config.getOptional[Boolean]("app.productionScope").getOrElse(true)

  private[this] val scheduler: Scheduler = schedulerProvider.get()

  schedulerProvider.get().setJobFactory(jobFactory)

  private[this] def dailyAtHourAndMinute(name: String,
                                         group: String,
                                         hour: Int = 0,
                                         minute: Int = 0): CronTrigger = {
    TriggerBuilder
      .newTrigger()
      .withIdentity(name, group)
      .withSchedule(
        CronScheduleBuilder.dailyAtHourAndMinute(hour, minute)
      ).build
  }

  private[this] def weeklyAtHourAndMinute(name: String,
                                          group: String,
                                          weekDays: Seq[IWeekDay] = Seq(Sat),
                                          hour: Int = 0,
                                          minute: Int = 0): CronTrigger = {

    val days: Seq[Integer] = weekDays.map { d => int2Integer(d.ord) }

    TriggerBuilder
      .newTrigger()
      .withIdentity(name, group)
      .withSchedule(
        CronScheduleBuilder.atHourAndMinuteOnGivenDaysOfWeek(hour, minute, days: _*)
      ).build
  }

  private[this] def scheduleJob(jobDetail: JobDetail, trigger: Trigger): Date = {
    val result = scheduler.scheduleJob(jobDetail, trigger)
    Try {
      Logger.info(s"QuartzJobExecutor: ${jobDetail.getJobClass.getName} scheduled")
    }
    result
  }

  private[this] def runImmediately(jobDetail: JobDetail): Unit = {
    scheduler.addJob(jobDetail, true)
    scheduler.triggerJob(jobDetail.getKey)
  }


  if (productionScope) {

   //Add jobs here for the production scope

  } else {
    Logger.info(s"QuartzJobExecutor: all jobs are ignored in test scope")
  }


}
