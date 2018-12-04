import Client._
import Server.Join
import akka.actor.{Actor, ActorRef}
import akka.util.Timeout
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._

class Client extends Actor {
  implicit val timeout = Timeout(10 second)
  context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

  def receive = {
    case Joined =>
      Platform.runLater {

      }
      context.become(joined)

    case SentJoin(ip, port) =>
      //sent join to server
      val serverRef = context.actorSelection(s"akka.tcp://ball@$ip:$port/user/server")
      serverRef ! Join
    case _=>
  }
  def joined : Receive = {
    case Begin(starthand) =>
      Platform.runLater{
        for (x <- starthand){
          hand += x
        }
      }
      sender ! "Ready"
    case yourTurn(c) =>
      Platform.runLater{
        hand += c
      }
    // sender ! play card

    case _=>
  }
}
object Client {
  var joinList: Option[Iterator[ActorRef]] = None
  var hand = new ObservableBuffer[Card]()
  case class Joined()
  case class SentJoin(ip: String, port: String)
  case class Begin(starthand: ObservableBuffer[Card])
  case class yourTurn(card: Card)
}
