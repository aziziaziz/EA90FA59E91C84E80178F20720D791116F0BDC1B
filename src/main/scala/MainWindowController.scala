import java.net.InetAddress

import Client.StartJoin
import akka.actor.ActorRef
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{AnchorPane, HBox, Pane}
import scalafxml.core.macros.sfxml
import scalafx.Includes._

@sfxml
class MainWindowController(
                            private val image_banner: ImageView,
                            private val image_pane: Pane,
                            private val join_server_ip: TextField,
                            private val join_server_port: TextField,
                            private val host_connection_list: ComboBox[InetAddress],
                            private val client_connection_list: ComboBox[InetAddress],
                            private val join_server_name: TextField,
                            private val join_pane: AnchorPane,
                            private val connect: ComboBox[InetAddress],
                            private val connection_pane: AnchorPane,
                            private val host_ip: Label,
                            private val host_port: Label,
                            private val host_panel_info: HBox
                          ) {
  var clientActorRef: Option[ActorRef] = None

  // used to initialize the main window view with uno images and stuff
  def init(): Unit = {
    val image = new Image("images/uno.png")
    image_banner.image = image
    // this is where the user can select the connection available, recall var tr from game class
    connect.setItems(game.tr)
  }

  init()

  // method is run when user clicks on connect
  def connectS(): Unit = {
    if (!connect.getSelectionModel.isEmpty) {
      game.initConnect(connect.getSelectionModel.getSelectedItem)
      connection_pane.disable = true
      connection_pane.visible = false
      join_pane.disable = false
      join_pane.visible = true
      host_panel_info.visible = true
      host_panel_info.disable = false
      host_ip.text = game.ipaddress.getHostAddress
      host_port.text = AddressExtension.portOf(game.system).toString
    }
  }

  // this is run when user click on join game button, after that user is directed to lobby
  def connectGame(): Unit = {
    clientActorRef foreach { ref =>
      ref ! StartJoin(join_server_ip.text.value, join_server_port.text.value, join_server_name.text.value)
    }
  }
}
