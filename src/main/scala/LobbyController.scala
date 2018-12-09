import akka.actor.ActorRef
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{Label, TableView}
import scalafx.scene.layout.HBox
import scalafxml.core.macros.sfxml

@sfxml
class LobbyController(
                     host_info_panel: HBox,
                     host_ip: Label,
                     host_port: Label,
                     user_list: TableView[String]
                     ) {
  def init(): Unit = {
    host_ip.text = game.ipaddress.getHostAddress
    host_port.text = AddressExtension.portOf(game.system).toString
    println()
  }

  init()

  game.clients.onChange((x, y) => {
    user_list.columns ++ "asdasd"
    Platform.runLater {
      //user_list.items = new ObservableBuffer() ++= x.toList
      println("-----------------------------------------")
      //game.clients.foreach(temp => {temp.path.name})
      game.clients.foreach(tep => {println(tep.path.name.toString)})
      println(game.clients.foreach(temp => {temp.path.name.toString}))
      println("-----------------------------------------")
    }
  })

  def updateList(): Unit = {
    println("Update Listsssssssssssssssssss")
  }
}
