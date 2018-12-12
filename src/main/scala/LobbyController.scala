
import Client.StartGame
import Server.StartP
import akka.actor.ActorRef
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{Button, Label, ListView, TableView}
import scalafx.scene.layout.HBox
import scalafxml.core.macros.sfxml

@sfxml
class LobbyController(
                     host_info_panel: HBox,
                     host_ip: Label,
                     host_port: Label,
                     user_list: ListView[String],
                     lobby_start: Button
                     ) {
  def init(): Unit = {
    host_ip.text = game.ipaddress.getHostAddress
    host_port.text = AddressExtension.portOf(game.system).toString
  }

  init()

  // use to update the lobby list
  def showPlayers(allNames: List[Person]): Unit = {
    println("-------------------addedPlayers-------------------------")
    val users: ObservableBuffer[String] = new ObservableBuffer[String]()
    for (a <- allNames) users.+=(a.name)
    Platform.runLater {
      user_list.items = users
    }
  }

  // start implementing here, direct user to PlayWindow and start coding the game logic
  def startGame(): Unit = {
    game.serverRef ! StartP
  }
}
