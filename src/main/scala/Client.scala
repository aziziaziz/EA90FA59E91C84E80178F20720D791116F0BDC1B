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

class Client extends Actor {
  implicit val timeout = Timeout(10 second)
  context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])

  def receive = {
    case Joined =>
      Platform.runLater {
        MyPassBall.control.displayStatus("Client Has Joined")
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
      Platform.runLater{
        MyPassBall.control.hideBall()
      }
      joinList = Option(Iterator.continually(list.toList.filterNot(x=> x == self)).flatten)
      sender ! "Ready"
    case Take =>
      Platform.runLater{
        MyPassBall.control.showBall()
      }
      sender ! "Taken"
    case PassBall=>
      //pass to someone in the group
      val cirlist = joinList.get.take(1).toList
      for (x <- cirlist){
        val result = x ? Take
        result.foreach{x =>
          MyPassBall.control.hideBall()
        }
      }
    case DisassociatedEvent(localAddress, remoteAddress, _) =>
      Platform.runLater {
        MyPassBall.createAlert("Game finished due to Person left")
      }
      MyPassBall.control.showBall()

      context.unbecome()
    case _=>
  }
}
object Client {
  var joinList: Option[Iterator[ActorRef]] = None
  case object Joined
  case class SentJoin(ip: String, port: String)
  case class Begin(clients: Iterable[ActorRef])
  case object Take
  case object PassBall
}