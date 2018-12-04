import Client.{Begin, Joined, yourTurn}
import Server.{Join, Start}
import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import scalafx.collections.ObservableBuffer

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Server extends Actor{
  implicit val timeout: Timeout = Timeout(20 second)
  var clientIterator: Option[Iterator[ActorRef]] = None
  def receive = {
    case Join =>
      sender ! Joined
    case Start =>
      context.become(started)
      for(x <- game.clients){
        var starthand = for (x <- (0 to 5)) yield {game.a.remove(0)}
        sender ! starthand.toList
      }
      var turn = 0
      var order = for (x <- game.clients) yield { turn = turn + 1
        (x, turn)}
    //choose order
    // sender ? yourTurn(Card)
      //

    case _=>
  }
  def started: Receive = {
    case _=>
      println("discard message")
  }
}
object Server {
  case object Join
  case object Start

}

