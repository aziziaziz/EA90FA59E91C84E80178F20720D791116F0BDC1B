import Client.{Begin, Draw, Joined, Win}
import Server._
import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout
import scalafx.collections.ObservableBuffer

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

class Server extends Actor{
  implicit val timeout: Timeout = Timeout(20 second)
  var players = new ObservableBuffer[ActorRef]()
  var turnCount = 0
  var next = 1
  var discardPile = ""
  var deck = new ObservableBuffer[String]()

  var clientIterator: Option[Iterator[ActorRef]] = None
  def receive = {
    case Join(some: String) =>
      println("Server Receive: " + some)
      if (some.equals("Host")) {
        game.hosts += sender
      } else {
        game.clients += sender
      }
      sender ! Joined("asdsadsadasd")
    case Start =>
      context.become(started)
      deck = Random.shuffle(deckRef)
      var turn = 0
      var next = 1

      game.clients.foreach(x => players.add(x))

      //giving the starting hand
      for(i <- Range(0,4)){
        for(j <- Range(0, 8)){
          players.get(i) ? Draw(deck.remove(0))
        }
      }

      players.get(turnCount) ? Turn

    case _=>
  }
  def started: Receive = {

    case Reverse(c) =>
      //update discard pile for server
      discardPile = c

      //update discard pile for players
      for(i <- Range(0, 4); i != turnCount)
        players.get(i) ! UpdateField(discardPile)

      //calculation for changing order
      next = if (next == 1) -1 else 1

      //giving the next turn
      turnCount = turnCount + next
      if (turnCount == -1)
        turnCount = 3
      else if (turnCount == 4)
        turnCount = 0
      players.get(turnCount) ? Turn

    case Skip(c) =>
      //update discard pile for server
      discardPile = c

      //update discard pile for players
      for(i <- Range(0, 4); i != turnCount)
        players.get(i) ! UpdateField(discardPile)

      //some calculations for skipping turn
      if (next == 1)
        turnCount = if (turnCount+2>3)turnCount-3 else turnCount + 1
      else
        turnCount = if (turnCount-2<0) turnCount+3 else turnCount -1

      //giving the next turn
      turnCount = turnCount + next
      if (turnCount == -1)
        turnCount = 3
      else if (turnCount == 4)
        turnCount = 0
      players.get(turnCount) ? Turn

    case DrawMore(c) =>
      //update discard pile for server
      discardPile = c

      //update discard pile for players
      for(i <- Range(0, 4); i != turnCount)
        players.get(i) ! UpdateField(discardPile)

      //giving out cards
      for(i <- Range(0, c.charAt(0).toInt)) {
        if (deck.length == 0){
          deck = Random.shuffle(deckRef)
        }
        players.get(turnCount) ? Draw(deck.remove(0))
      }

      //skip turn for draw 4
      if(c.charAt(0).equals('4')){
        if (next == 1)
          turnCount = if (turnCount+2>3)turnCount-3 else turnCount + 1
        else
          turnCount = if (turnCount-2<0) turnCount+3 else turnCount -1
      }
      //giving the next turn
      turnCount = turnCount + next
      if (turnCount == -1)
        turnCount = 3
      else if (turnCount == 4)
        turnCount = 0
      players.get(turnCount) ? Turn

    case ChangeColour(c) =>
      //update discard pile for server
      discardPile = c

      //update discard pile for players
      for(i <- Range(0, 4); i != turnCount)
        players.get(i) ! UpdateField(discardPile)

      //giving the next turn
      turnCount = turnCount + next
      players.get(turnCount) ? Turn

    case Normal(c) =>
      //update discard pile for server
      discardPile = c

      //update discard pile for players
      for(i <- Range(0, 4); i != turnCount)
        players.get(i) ! UpdateField(discardPile)

      //giving the next turn
      turnCount = turnCount + next
      if (turnCount == -1)
        turnCount = 3
      else if (turnCount == 4)
        turnCount = 0
      players.get(turnCount) ? Turn

    case Win =>
      val winner:ActorRef = (for{i <- players; i.equals(sender())} yield i).remove(0)
      //send each player which bitch has won


    case _=>
      println("discard message")
  }
}
object Server {
  var deckRef = new ObservableBuffer[String]()
  var colour = Array("b", "g", "r", "y")
  for (i <- Range(0, 2)){
    for(j <- colour){
      for(k <- Range(1,10))
        deckRef += j.toString + k.toString
    }
    for(j <- colour){
      deckRef += j.toString + "s"
      deckRef += j.toString + "r"
      deckRef += j.toString + "+"
    }
  }
  for(i <- colour){
    deckRef += i.toString + "0"
  }
  for(i <- Range(0, 4)){
    deckRef += "4+"
    deckRef += "cc"
  }
  case class Join(some: String)
  case object Start
  case class Reverse(c : String)
  case class Skip(c : String)
  case class ChangeColour(colour : String)
  case class Normal(c:String)
  case class DrawMore(c : String)
  case object Turn
  case class UpdateField(discardPile : String)

}

