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
                     user_list: ListView[ActorRef],
                     lobby_start: Button
                     ) {
  def init(): Unit = {
    host_ip.text = game.ipaddress.getHostAddress
    host_port.text = AddressExtension.portOf(game.system).toString
    println()

    if (game.typesS != "HOST") {
      host_info_panel.disable = true
      host_info_panel.visible = false
      lobby_start.disable = true
      lobby_start.visible = false
    }
  }

  init()

  game.clients.onChange((x, y) => {
    Platform.runLater {
      //user_list.items = new ObservableBuffer() ++= x.toList
      println("-----------------------------------------")
      //game.clients.foreach(temp => {temp.path.name})
      user_list.items = new ObservableBuffer() ++= x.toList
      println(game.clients.foreach(temp => {temp.path.name.toString}))
      println("-----------------------------------------")
    }
  })

  def updateList(): Unit = {
    game.clients.onChange((x, y) => {
      Platform.runLater {
        //user_list.items = new ObservableBuffer() ++= x.toList
        println("-----------------------------------------")
        //game.clients.foreach(temp => {temp.path.name})
        user_list.items = new ObservableBuffer() ++= x.toList
        println(game.clients.foreach(temp => {temp.path.name.toString}))
        println("-----------------------------------------")
      }
    })
  }

  def startGame(): Unit = {

  }
}
