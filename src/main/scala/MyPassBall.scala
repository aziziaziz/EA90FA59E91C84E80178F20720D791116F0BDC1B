import java.net.NetworkInterface

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._
import scala.collection.JavaConverters._
import scalafx.collections.ObservableHashSet
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object MyPassBall extends JFXApp{

  val clients = new ObservableHashSet[ActorRef]()
  //Test Mk
  //another test
  //aertyeab6y4eqtqtrgqer

  var count = -1
  val addresses = (for (inf <- NetworkInterface.getNetworkInterfaces.asScala;
                        add <- inf.getInetAddresses.asScala) yield {
    count = count + 1
    (count -> add)
  }).toMap
  for((i, add) <- addresses){
    println(s"$i = $add")
  }
  println("please select which interface to bind")
  var selection: Int = 0
  do {
    selection = scala.io.StdIn.readInt()
  } while(!(selection >= 0 && selection < addresses.size))

  val ipaddress = addresses(selection)
  //arstearewterateartargaergear
  val overrideConf = ConfigFactory.parseString(
    s"""
       |akka {
       |  loglevel = "INFO"
       |
 |  actor {
       |    provider = "akka.remote.RemoteActorRefProvider"
       |  }
       |
 |  remote {
       |    enabled-transports = ["akka.remote.netty.tcp"]
       |    netty.tcp {
       |      hostname = "${ipaddress.getHostAddress}"
       |      port = 0
       |    }
       |
 |    log-sent-messages = on
       |    log-received-messages = on
       |  }
       |
 |}
       |
     """.stripMargin)


  val myConf = overrideConf.withFallback(ConfigFactory.load())
  val system = ActorSystem("ball", myConf)


  //create server actor
  val server = system.actorOf(Props[Server], "server")
  //create client actor
  val client = system.actorOf(Props[Client], "client")



  val resource = getClass.getResourceAsStream("Window.fxml")

  val loader = new FXMLLoader(null, NoDependencyResolver)
  loader.load(resource)

  val ui = loader.getRoot[javafx.scene.layout.BorderPane]
  val control = loader.getController[WindowController#Controller]()

  stage = new PrimaryStage(){
    scene = new Scene(){
      root = ui
    }
  }
  stage.onCloseRequest = handle {
    system.terminate
  }

  def createAlert(k: String): Unit = {
    new Alert(AlertType.Error) {
      initOwner(stage)
      title = "somebody left"
      headerText = "Game Finish"
      contentText = k
    }.showAndWait()
  }

}
