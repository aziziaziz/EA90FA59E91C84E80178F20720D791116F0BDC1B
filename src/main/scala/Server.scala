import Client.{Begin, Joined, Take}
import Server.{Join, Start}
import akka.actor.{Actor, ActorRef}
import io.aeron.driver.Sender
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
class Server extends Actor{
  implicit val timeout: Timeout = Timeout(20 second)
  var clientIterator: Option[Iterator[ActorRef]] = None
  def receive = {
    //test again
    case Join =>
      MyPassBall.clients += sender
      sender ! Joined
    case Start =>
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

