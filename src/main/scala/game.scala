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
  // Address code - use to find the list of connections available for the user PC, to supply value for the connection combo box
  var count = -1
  var selection: Int = 0

  val addresses = (for (inf <- NetworkInterface.getNetworkInterfaces.asScala;
                        add <- inf.getInetAddresses.asScala) yield {
    count = count + 1
    (count -> add)
  }).toMap

  // This is the variable that stores all the connection for the user pc, var tr
  var tr: ObservableBuffer[InetAddress] = (for (inf <- NetworkInterface.getNetworkInterfaces.asScala;
                                                add <- inf.getInetAddresses.asScala) yield {
    add
  }).to
  // Address code - end

  // Establish con - Used to establish connection based on user selected connection combo box value
  var system: ActorSystem =_
  var serverRef: ActorRef =_
  var clientRef: ActorRef =_
  var ipaddress: InetAddress =_

  // this is run when user click connect button
  def initConnect(some: InetAddress): Unit = {
    ipaddress = some
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

    // create system with uno name
    system = ActorSystem("uno", myConf)
    //create server actor
    serverRef = system.actorOf(Props[Server](), "server")
    //create client actor
    clientRef = system.actorOf(Props[Client], "client")

    // Establish con - end

    // this is to store created client in the WindowController
    mainWindowControl.clientActorRef = Option(clientRef)
  }

  //Main Window View - For user to "Host" or "Join" a game
  val mainWindow = getClass.getResourceAsStream("MainWindow.fxml")
  //Main Window Loader
  val mainWindowloader = new FXMLLoader(null, NoDependencyResolver)
  //Loads main window
  mainWindowloader.load(mainWindow)
  //Get main window root
  var mainWindowRoot = mainWindowloader.getRoot[javafx.scene.layout.BorderPane]
  //Get main window controller
  val mainWindowControl = mainWindowloader.getController[MainWindowController#Controller]()

  // Load Primary Scene - Load the first scene
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

  // Load Primary Scene - end

  def createAlert(k: String): Unit = {
    new Alert(AlertType.Error) {
      initOwner(stage)
      title = "somebody left"
      headerText = "Game Finish"
      contentText = k
    }.showAndWait()
  }

  // Load Lobby - used to load lobby when user entered their username, host IP and Host Port
  var lobbyWindow = getClass.getResourceAsStream("Lobby.fxml")
  var lobbyLoader: FXMLLoader =_
  var lobbyRoot: BorderPane =_
  var lobbyWindowControl: LobbyController#Controller =_
  var typesS: String = ""
  // this is run when user clicks on join game
  def enterLobby(types: String): Unit = {
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

  // load lobby - end

  // Load play scene - this is the code to load play scene
  def newGame(): Unit = {
    //Play Window - Game Stage
    val testPlay = getClass.getResourceAsStream("PlayWindow.fxml")
    //Play Window Loader
    val testPlayLoader = new FXMLLoader(null, NoDependencyResolver)
    //Loads play window
    testPlayLoader.load(testPlay)
    //Get play window root
    var playUI = testPlayLoader.getRoot[javafx.scene.layout.BorderPane]
    //Get play window controller
    val testPlayControl = testPlayLoader.getController[PlayWindowController#Controller]()

    stage.scene = new Scene() {
      root = playUI
    }
  }

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
}