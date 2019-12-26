package concurrency

import java.util.concurrent.TimeUnit

import org.scalactic.source
import org.scalactic.source.Position._
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Millis, Minutes, Seconds, Span}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Awaitable}

trait ConcurrentFixture extends Eventually {

  private val duration: Duration = Duration(1, TimeUnit.MINUTES)

  def sync[T](f: Awaitable[T]): T = Await.result(f, duration)


  implicit val _config: PatienceConfig = PatienceConfig(
    timeout = scaled(Span(20, Seconds)),
    interval = scaled(
      Span(15, Millis)
    )
  )

  implicit val _pos: source.Position = here

  def Eventually[T](fun: => T): T = super.eventually(fun)(
    _config,
    _pos
  )

  /**
    * Please, run tests with EventuallyLong only on local environments! THANK YOU!
    **/
  def EventuallyLong[T](fun: => T): T = super.eventually(fun)(
    PatienceConfig(
      timeout = scaled(Span(1, Minutes)),
      interval = scaled(
        Span(200, Millis)
      )
    ),
    pos = _pos
  )
}
