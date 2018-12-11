import Client._
import Server.{BroadcastPlayers, Join, sendList}
import akka.actor.{Actor, ActorRef, ActorSelection}
import scalafx.application.Platform
import akka.pattern.ask
import akka.remote.DisassociatedEvent
import akka.util.Timeout
import scalafx.collections.ObservableHashSet

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scalafx.scene.control.Alert

class Client extends Actor {
  implicit val timeout = Timeout(10 second)
  context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])
  var allPlayers = List.empty[Person]
  var serverRef: ActorSelection =_

  def receive = {
    case StartJoin(server, port, name)=>
      serverRef = context.actorSelection(s"akka.tcp://uno@$server:$port/user/server")
      val result  = serverRef ? Join(self, name)
      result.foreach( x => {
        if (x == Client.Joined){
          serverRef ! BroadcastPlayers
          context.become(joined)
          Platform.runLater(() => {
//            MyPassBall.showLobby()
            game.enterLobby("Some")
          })
        } else {
          Platform.runLater {

          }
        }
      })

    case _=>
  }

  def joined: Receive = {
    case StartGamePrint =>
      println("GAME STARTED")

    case AcceptPlayers(players) =>
      allPlayers = players
      println(allPlayers.size + " PLAYER SIZE IN CLIENT")
      Platform.runLater {
        game.lobbyWindowControl.showPlayers(allPlayers)
      }

    case StartGame =>
      println("Client Started")
      context.become(GameStart)
  }

  def GameStart: Receive = {
    case Readyss =>
      println("Client Started")
    case _=>
  }

  def Ready : Receive = {
    case Begin(list) =>
      sender ! "Ready"
    case Draw(c) =>
      /*
      Platform.runLater{
        hand += c
      }
      sender ! "Drawn"*/
    case myTurn=>

    case _=>
  }
}
object Client {
  var joinList: Option[Iterator[ActorRef]] = None
  var memberList: Option[Iterable[Person]] = None
  var memberIter: Option[Iterator[ActorRef]] = None
  case class StartJoin(serverIp: String, port: String,name: String)
  case object StartGamePrint
  case class AcceptPlayers(players: List[Person])
  case object StartGame
  case object Readyss

  case object Joined
  case object Ready
  case object Start
  //case class SentJoin(ip: String, port: String, status: String, types: String)
  case class Begin(clients: Iterable[ActorRef])
  case object Take
  case object PassBall
  case object HostGame
  case class Draw(c : Card)
  case class UpdateList(some: ActorRef)
  case object myTurn
  case object yourTurn
  case class Play(some: ActorRef)

}

