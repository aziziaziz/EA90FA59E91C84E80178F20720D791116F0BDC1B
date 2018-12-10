import Client.{HostGame, Joined, SentJoin}
import Server.Join
import akka.actor.{Actor, ActorRef}
import scalafx.application.Platform
import akka.pattern.ask
import akka.remote.DisassociatedEvent
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scalafx.scene.control.Alert

class Client extends Actor {
  implicit val timeout = Timeout(10 second)
  context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

  def receive = {
    case Joined(some: String) =>
      println("Client Joined")

      Platform.runLater {
        //game.control.displayStatus(some + " Has Joined")
        game.control.Statusu(some + " Has Joined")
        game.showUser()
      }
      context.become(standBy)

    case SentJoin(ip, port, name, types) =>
      println("Client, ip: " + ip + ", port: " + port + ", status: " + name)
      //sent join to server
      val serverRef = context.actorSelection(s"akka.tcp://$name@$ip:$port/user/server")
      serverRef ! Join("NEWJOIN")

    case _=>
  }

  def standBy : Receive = {


    case _=>
  }
  /*
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
  */
}
object Client {
  var joinList: Option[Iterator[ActorRef]] = None
  case class Joined(some: String)
  case class SentJoin(ip: String, port: String, status: String, types: String)
  //case class Joined(startHand: List[Card])
  case class Begin(clients: Iterable[ActorRef])
  case object Take
  case object PassBall
  case object HostGame
  case class Draw(c : Card)
  case object myTurn
  case object yourTurn
}

