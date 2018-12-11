import Client._
import Server.{BroadcastPlayers, Join, Start}
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

    case Start =>
      println("Server Start")
      for (p <- Server.players) {
        println("P print")
        p.ref ! StartGame
      }

      context.become(started)

//      for(x <- game.clients){
//        var starthand = for (x <- (0 to 7)) yield {game.a.remove(0)}
//        sender ! starthand.toList
//      }
//      var turn = 0
//      var order = for (x <- game.clients) yield { turn = turn + 1
//        (x, turn)}
      /*
      context.become(started)
      val feedbacks: Iterable[Future[Any]] = for (client <- MyPassBall.clients) yield {
        client ? Begin(MyPassBall.clients.toList)
      }
      for(result <- feedbacks){
        val mainFuture: Future[String] = Future {
          for(reply <- result){
            reply
          }
          "done"
        }
        mainFuture.foreach { x=>
          if (!clientIterator.isDefined){
            clientIterator = Option(Iterator.continually(MyPassBall.clients.toList).flatten )
          }
          for(iter <- clientIterator){
            val client: Iterator[ActorRef] = iter.take(1)
            var tclient: ActorRef = client.next()

            val cresult = tclient ? Take
            var result = Await.result(cresult, 10 second)
            while(result != "Taken"){
              tclient = client.next()
              val cresult = tclient ? Take
              result = Await.result(cresult, 10 second)
            }
          }
        }
      }
      */

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
  case object Start
  case object sendList
  val players = new ObservableHashSet[Person]
}

