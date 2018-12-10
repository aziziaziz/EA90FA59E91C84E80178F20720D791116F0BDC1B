import java.net.{InetAddress, NetworkInterface}

import akka.actor.{ActorRef, ActorSystem, Address, ExtendedActorSystem, Extension, ExtensionId, Props}
import com.typesafe.config.{Config, ConfigFactory}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafx.Includes._

import scala.collection.JavaConverters._
import scalafx.collections.{ObservableBuffer, ObservableHashSet}
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.layout.BorderPane

object game extends JFXApp{
  var addresses: Map[Int, InetAddress] =_
  var ipaddress: InetAddress =_
  var myConf: Config =_
  var overrideConf: Config =_
  var system: ActorSystem =_
  var tr: ObservableBuffer[InetAddress] =_
  var server: ActorRef =_
  var client: ActorRef =_

  def initializeConnect(name: String, ip: InetAddress): Unit = {
    ipaddress = ip

    overrideConf = ConfigFactory.parseString(
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

    myConf = overrideConf.withFallback(ConfigFactory.load())
    system = ActorSystem(name, myConf)

    //create server actor
    server = system.actorOf(Props[Server], "server")
    //create client actor
    client = system.actorOf(Props[Client], name)
  }

  // Akka
  tr = (for (inf <- NetworkInterface.getNetworkInterfaces.asScala;
       add <- inf.getInetAddresses.asScala) yield {
    add
  }).to

  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  var a = new ObservableBuffer[Card]
  for (x <- (0 to 108)){
    a.add(new Card('x','t'))
  }

  val clients = new ObservableHashSet[ActorRef]()
  val hosts = new ObservableHashSet[ActorRef]()

  //deck creation
  var deck = new ObservableBuffer[String]()
  var colour = Array("b", "g", "r", "y")
  for (i <- Range(0, 2)){
    for(j <- colour){
      for(k <- Range(1,10))
        deck += j.toString + k.toString
    }
    for(j <- colour){
      deck += j.toString + "s"
      deck += j.toString + "r"
      deck += j.toString + "+"
    }
  }

  for(i <- colour){
    deck += i.toString + "0"
  }

  for(i <- Range(0, 4)){
    deck += "4+"
    deck += "cc"
  }

  //Dr Chin's Pass Ball View
  val windows2 = getClass.getResourceAsStream("Window.fxml")
  //Dr Chin's PassBall Loader
  val loader2 = new FXMLLoader(null, NoDependencyResolver)
  //Dr Chin's loader
  loader2.load(windows2)
  //Dr Chin's getRoot
  var ui2 = loader2.getRoot[javafx.scene.layout.BorderPane]
  //Dr Chin's get controller
  val control = loader2.getController[WindowController#Controller]()

  //Main Window View - For user to "Host" or "Join" a game
  val mainWindow = getClass.getResourceAsStream("MainWindow.fxml")
  //Play Window - Game Stage
  val testPlay = getClass.getResourceAsStream("PlayWindow.fxml")

  //Main Window Loader
  val mainWindowloader = new FXMLLoader(null, NoDependencyResolver)
  //Play Window Loader
  val testPlayLoader = new FXMLLoader(null, NoDependencyResolver)

  //Loads main window
  mainWindowloader.load(mainWindow)
  //Loads play window
  testPlayLoader.load(testPlay)

  //Get main window root
  var mainWindowRoot = mainWindowloader.getRoot[javafx.scene.layout.BorderPane]
  //Get play window root
  var playUI = testPlayLoader.getRoot[javafx.scene.layout.BorderPane]

  //Get main window controller
  val mainWindowControl = mainWindowloader.getController[MainWindowController#Controller]()
  //Get play window controller
  val testPlayControl = testPlayLoader.getController[PlayWindowController#Controller]()

  stage = new PrimaryStage(){
    scene = new Scene() {
      root = mainWindowRoot
    }
    title = "Uno"
  }

  stage.setResizable(false)

  stage.onCloseRequest = handle {
    if (system != null) {
      system.terminate
    }
  }

  def createAlert(k: String): Unit = {
    new Alert(AlertType.Error) {
      initOwner(stage)
      title = "somebody left"
      headerText = "Game Finish"
      contentText = k
    }.showAndWait()
  }

  var lobbyWindow = getClass.getResourceAsStream("Lobby.fxml")
  var lobbyLoader: FXMLLoader =_
  var lobbyRoot: BorderPane =_
  var lobbyWindowControl: LobbyController#Controller =_
  var typesS: String = ""

  def enterLobby(types: String): Unit = {
    //Lobby - "Host" starts game when all players are in
    //lobbyWindow = getClass.getResourceAsStream("Lobby.fxml")
    //Lobby Loader
    typesS = types
    lobbyLoader = new FXMLLoader(null, NoDependencyResolver)
    //Loads lobby window
    lobbyLoader.load(lobbyWindow)
    //Get lobby window root
    lobbyRoot = lobbyLoader.getRoot[javafx.scene.layout.BorderPane]
    //Get lobby window controller
    lobbyWindowControl = lobbyLoader.getController[LobbyController#Controller]()

    stage.scene = new Scene() {
      root = lobbyRoot
    }
  }

  def showUser(): Unit = {
    println("HOST")
    hosts.foreach(x=>println(x.toString()))
    println("\nCLIENT")
    clients.foreach(x=>println(x.toString()))
  }
}