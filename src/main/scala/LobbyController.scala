import scalafx.scene.control.Label
import scalafx.scene.layout.HBox
import scalafxml.core.macros.sfxml

@sfxml
class LobbyController(
                     host_info_panel: HBox,
                     host_ip: Label,
                     host_port: Label
                     ) {
  def init(): Unit = {
    host_ip.text = game.ipaddress.getHostAddress
    host_port.text = AddressExtension.portOf(game.system).toString
  }

  init()

  def updateList(): Unit = {
    println("Update Listsssssssssssssssssss")
  }
}
