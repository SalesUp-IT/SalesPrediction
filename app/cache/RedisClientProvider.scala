package cache

import java.net.URI

import com.google.inject.Provider
import com.redis.RedisClient
import javax.inject.Inject
import play.api.Configuration

@javax.inject.Singleton
class RedisClientProvider @Inject()(config: Configuration) extends Provider[RedisClient] {

  private val redisUri = config.getOptional[String]("redis.uri").getOrElse("redis://localhost:6379")

  override def get(): RedisClient = new RedisClient(new URI(redisUri))

  def withRedisClient[A](client: RedisClient => A): A = {
    val _client = get()
    val result = client.apply(_client)
    _client.quit
    result
  }
}
