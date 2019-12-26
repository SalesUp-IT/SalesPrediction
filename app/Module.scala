import com.google.inject.AbstractModule
import quartz.QuartzJobExecutor

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ApplicationHooks]).asEagerSingleton()
    bind(classOf[QuartzJobExecutor]).asEagerSingleton()
  }
}
