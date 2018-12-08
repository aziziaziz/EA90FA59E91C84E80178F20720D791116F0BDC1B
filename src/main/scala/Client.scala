import Client.{Update, _}
import Server._
import akka.actor.{Actor, ActorRef}
import scalafx.application.Platform
import akka.pattern.ask
import akka.remote.DisassociatedEvent
import akka.util.Timeout
import scalafx.collections.ObservableBuffer

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scalafx.scene.control.Alert

class Client extends Actor {
  implicit val timeout = Timeout(10 second)
  context.system.eventStream.subscribe(self, classOf[akka.remote.DisassociatedEvent])
  var hand = new ObservableBuffer[String]()
  var discardPile = ""

  def receive = {
    case Joined(some: String) =>
      println("Client Joined")

      Platform.runLater {
        //game.control.displayStatus(some + " Has Joined")
        game.control.Statusu(some + " Has Joined")
        game.showUser()


      }
      //context.become(joined)

    case SentJoin(ip, port, status) =>
      println("Client -------------" + ip + port + status)
      //sent join to server
      val serverRef = context.actorSelection(s"akka.tcp://ball@$ip:$port/user/server")
      serverRef ! Join(status)

    case _=>
  }

  def joined : Receive = {
    case Draw(s) =>
      Platform.runLater{
        hand += s
      }
      sender ! "Drawn"
    case Turn=>
      Platform.runLater {
        var x = true

        //var play = card that was played
        while (x){
          /*
        if(play.charAt(0).equals(discardPile.charAt(0)) || play.charAt(1).equals(discardPile.charAt(1)) || play.equals("cc") ||
          play.equals("4+") || discardPile.equals("")){
          if(play.charAt(1).toInt > 58 || play.charAt(1) == 43){
            if(play.charAt(1).equals('r')){
              sender() ! Reverse(play)
            }else if (play.charAt(1).equals('s')){
              sender() ! Skip(play)
            }else if (play.charAt(1).equals('+')){
               ! DrawMore(play)
            } else{
              sender() ! ChangeColour(play)
            }
            x = false
          }else{
            sender() ! Normal(play)
          }
        }else if (click draw button ){
          sender() ! DrawMore("1+")
          x = false
        }else{
          println("cant play this shit beetch")
        }*/
      }


        if (hand.length == 0){
          sender() ! Win
        }
      }
    case UpdateField(discardPile) =>
      Platform.runLater{
      this.discardPile = discardPile
    }

    case _=>
  }

}
object Client {
  val Name : String = "ssssss"
  case class Joined(some: String)
  case class SentJoin(ip: String, port: String, status: String)
  case class Begin(clients: Iterable[ActorRef])
  case object Take
  case object PassBall
  case object HostGame
  case class Draw(s : String)
  case object Win
}

