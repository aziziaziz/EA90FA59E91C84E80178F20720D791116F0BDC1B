import java.net.NetworkInterface

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._

import scala.collection.JavaConverters._
import scalafx.collections.{ObservableBuffer, ObservableHashSet}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object game extends JFXApp{

  var a = new ObservableBuffer[Card]
  for (x <- (0 to 108)){
    a.add(new Card('x','t'))
  }
  val clients = new ObservableHashSet[ActorRef]()
  val hosts = new ObservableHashSet[ActorRef]()

  // Asd
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

  val resource = getClass.getResourceAsStream("MainWindow.fxml")
  val windows2 = getClass.getResourceAsStream("Window.fxml")
  val testPlay = getClass.getResourceAsStream("PlayWindow.fxml")

  val loader = new FXMLLoader(null, NoDependencyResolver)
  val loader2 = new FXMLLoader(null, NoDependencyResolver)
  val testPlayLoader = new FXMLLoader(null, NoDependencyResolver)

  loader.load(resource)
  loader2.load(windows2)
  testPlayLoader.load(testPlay)

  var ui = loader.getRoot[javafx.scene.layout.BorderPane]
  var ui2 = loader2.getRoot[javafx.scene.layout.BorderPane]
  var playUI = testPlayLoader.getRoot[javafx.scene.layout.BorderPane]

  val Micontrol = loader.getController[MainWindowController#Controller]()
  val control = loader2.getController[WindowController#Controller]()
  val testPlayControl = testPlayLoader.getController[PlayWindowController#Controller]()

  stage = new PrimaryStage(){
    scene = new Scene(){
      root = playUI
    }
  }

  stage.setResizable(false)
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

  def enterLobby(): Unit = {
    stage = new PrimaryStage() {
      scene = new Scene() {
        root = ui2
      }
    }
  }

  def showUser(): Unit = {
    println("HOST")
    hosts.foreach(x=>println(x.toString()))
    println("\nCLIENT")
    clients.foreach(x=>println(x.toString()))
  }
}
