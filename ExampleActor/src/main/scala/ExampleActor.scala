
import ExampleActor._
import akka.actor.ActorLogging
import akka.event.LoggingReceive
import akka.persistence.PersistentActor


object ExampleActor {

  case class CreateApple(id: String)
  case class GetAppleIfNoOrangesQuery(id: String)
  case class GetAppleIfNoOrangesResult(apple: Option[Int])
  case class AppleCreated(id: String)

  case class CreateOrange(id: String)
  case class OrangeCreated(id: String)

}

class AppleAggregator {

  var map = Map.empty[String, Int]

  def addApple(id: String, value: Int): Unit = {
    map += (id -> value)
  }

  def getApple(id: String) = map.get(id)

  def appleCount = map.size
}

class Status(private var status: String) {

  def setStatus(status: String) = this.status = status
  def getStatus = status

}

trait ExampleActorGenericFunctionality {

  val status: Status

}

trait AppleSupport {
  this: PersistentActor with ActorLogging with ExampleActorGenericFunctionality =>

  protected val apples = new AppleAggregator()

  val appleReceiver: Receive = {
    case CreateApple(id) => persist(AppleCreated(id))(onAppleCreated)
    case GetAppleIfNoOrangesQuery(id) => onGetAppleIfNoOranges(id)
  }

  private def onAppleCreated(e: AppleCreated) = {
    apples.addApple(e.id, (math.random * 100).toInt)
    onNewAppleCreated()
  }

  private def onGetAppleIfNoOranges(id: String) = {
    val apple = if (getNumberOfOranges == 0 && status.getStatus == "Running")
      apples.getApple(id)
    else
      None

    sender ! GetAppleIfNoOrangesResult(apple)
  }

  val appleRecover: Receive = {
    case AppleCreated(id) =>
  }

  protected def onNewAppleCreated()
  protected def getNumberOfOranges: Int
}

trait OrangeSupport {
  this: PersistentActor with ActorLogging =>

  val orangeReceiver: Receive = {
    case CreateOrange(id) => persist(OrangeCreated(id))(onOrangeCreated)
  }

  private def onOrangeCreated(e: OrangeCreated) = {
    log.debug("OrangeCreated: " + e.id)
  }

  val orangeRecover: Receive = {
    case e: OrangeCreated => onOrangeCreated(e)
  }

  protected def orangeCount = 10
}

class ExampleActor extends PersistentActor with ActorLogging with ExampleActorGenericFunctionality with AppleSupport with OrangeSupport {

  override def persistenceId: String = "ExampleActor"

  val receiveCommand: Receive = LoggingReceive {
    appleReceiver orElse orangeReceiver
  }

  val receiveRecover: Receive = LoggingReceive {
    appleRecover orElse orangeRecover
  }

  protected val status = new Status("Running")

  override protected def onNewAppleCreated() {
    if (apples.appleCount > orangeCount) {
      log.debug("Wow!")
    }
  }

  override protected def getNumberOfOranges: Int = orangeCount

}
