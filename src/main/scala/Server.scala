import Client._
import Server.{BroadcastPlayers, Join, StartP}
import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import scalafx.application.Platform
import scalafx.collections.{ObservableBuffer, ObservableHashSet}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Server extends Actor{
  implicit val timeout: Timeout = Timeout(20 second)
  var clientIterator: Option[Iterator[ActorRef]] = None
  def receive = {
    case Join(my, name) =>
      Server.players += new Person(my, name)
      sender() ! Joined

    case BroadcastPlayers =>
      println(Server.players.size + " PLAYER SIZE")

      val allPersons = Server.players.toList
      for (p <- Server.players) {
        println("P print")
        p.ref ! AcceptPlayers(allPersons)
      }


    case sendList =>
      for (temp <- game.clients) {
        sender ! UpdateList(temp)
      }

    case StartP =>
      println("Server Start")
//      for (p <- Server.players) {
//        println("P print")
//        p.ref ! Readyss
//      }

      //context.become(started)
    case _=>
  }
  def started: Receive = {
    case _=>
      println("discard message")
  }
}

case class Person(ref: ActorRef, name: String){
  override def toString: String = {
    name
  }
}

object Server {
  case class Join(myref: ActorRef, name: String)
  case object BroadcastPlayers
  case object StartP
  case object sendList
  val players = new ObservableHashSet[Person]
}

