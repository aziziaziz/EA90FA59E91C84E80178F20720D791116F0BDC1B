import Client._
import MyPassBall.system
import Server.Join
import akka.actor.{Actor, ActorRef}
import scalafx.application.Platform
import akka.pattern.ask
import akka.remote.DisassociatedEvent
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scalafx.scene.control.Alert

import scala.collection.mutable.ListBuffer

class Client extends Actor {
  implicit val timeout = Timeout(10 second)
  context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])
  var hand : ListBuffer[Card]

  def receive = {
    case Joined(startHand) =>
      Platform.runLater {
        for (x <- startHand){
          hand += x
        }
      }
      context.become(joined)

    case SentJoin(ip, port) =>
      //sent join to server
      val serverRef = context.actorSelection(s"akka.tcp://ball@$ip:$port/user/server")
      serverRef ! Join
    case _=>
  }
  def joined : Receive = {
    case Begin(list) =>
      sender ! "Ready"
    case Draw(c) =>
      Platform.runLater{
        hand += c
      }
      sender ! "Drawn"
    case myTurn=>

    case _=>
  }
}
object Client {
  var joinList: Option[Iterator[ActorRef]] = None
  case class Joined(startHand: List[Card])
  case class SentJoin(ip: String, port: String)
  case class Begin(clients: Iterable[ActorRef])
  case object Take
  case object PassBall
  case class Draw(c : Card)
  case object myTurn
}

case class Card (val colour: Char, val attribute: Char){
}
