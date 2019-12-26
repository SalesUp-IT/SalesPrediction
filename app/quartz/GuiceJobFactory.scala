package quartz


import com.google.inject.Injector
import javax.inject.Inject
import org.quartz.{Job, Scheduler}
import org.quartz.spi.{JobFactory, TriggerFiredBundle}

class GuiceJobFactory @Inject()(guice: Injector) extends JobFactory {

  private[this] val injector: Injector = guice

  override def newJob(bundle: TriggerFiredBundle, scheduler: Scheduler): Job = {

    val jobDetail = bundle.getJobDetail
    val jobClass = jobDetail.getJobClass

    try
      guice.getInstance(jobClass)
    catch {
      case e: Exception =>
        e.printStackTrace()
        throw new UnsupportedOperationException(e)
    }
  }
}
