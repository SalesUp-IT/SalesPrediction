package servers

trait IServer {

  def start(): Unit

  def shutdown(): Unit

}
