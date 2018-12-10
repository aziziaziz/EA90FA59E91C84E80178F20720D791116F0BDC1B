import java.net.InetAddress

import Client.SentJoin
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Button, ComboBox, TextField}
import scalafx.scene.image._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Pane
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.scene.control.Alert.AlertType

@sfxml
class MainWindowController(
                            private val image_banner: ImageView,
                            private val image_pane: Pane,
                            private val create_game: Button,
                            private val join_game: Button,
                            private val host_name: TextField,
                            private val join_server_ip: TextField,
                            private val join_server_port: TextField,
                            private val host_connection_list: ComboBox[InetAddress],
                            private val client_connection_list: ComboBox[InetAddress],
                            private val join_server_name: TextField
                          ) {
  def init(): Unit = {
    val image = new Image("images/uno.png")
    image_banner.image = image

    host_name.onInputMethodTextChanged = { _ =>
      create_game.disable = false
    }

    //create_game.text = host_name.text()
    if (host_name.text.toString() != null) {
      create_game.disable = false

      create_game.onMouseEntered = { _ =>
        enter(create_game)
      }

      create_game.onMouseExited = { _ =>
        leave(create_game)
      }
    } else {

    }

    join_game.onMouseEntered = { _ =>
      enter(join_game)
    }

    join_game.onMouseExited = { _ =>
      leave(join_game)
    }
    /*
    create_game.onMouseClicked = { _ =>
      game.client ! SentJoin("127.0.0.1", host_name.text.value, "CLIENT")
      game.enterLobby()
    }
    */
    host_connection_list.setItems(game.tr)
    client_connection_list.setItems(game.tr)
  }

  init()

  def enter(some: Button): Unit = {
    some.setStyle("-fx-border-color:gray; -fx-background-color: silver; -fx-border-width: 2; -fx-border-radius: 5; -fx-font-weight: bold; -fx-font-color: gray;")
  }

  def leave(some: Button): Unit = {
    some.setStyle("-fx-border-color:silver; -fx-background-color: none; -fx-border-width: 2; -fx-border-radius: 5; -fx-font-weight: bold; -fx-font-color: #6b6b6b;")
  }

  def hostGame(action: ActionEvent): Unit ={
    //reference to client ref
    //MyPassBall.client ! SentJoin(server.text.value, port.text.value)
    //game.client ! SentJoin("127.0.0.1", "11214", "HOST")
    if (host_name.text.value != "") {
      if (!host_connection_list.getSelectionModel.isEmpty) {
        game.initializeConnect(host_name.text.value, host_connection_list.getSelectionModel.getSelectedItem)
        game.client ! SentJoin(host_connection_list.getSelectionModel.getSelectedItem.getHostAddress, AddressExtension.portOf(game.system).toString, host_name.text.value, "HOST")
        game.enterLobby("HOST")
      } else {
        popUpAlert("Please select Host connection")
      }
    } else {
      popUpAlert("Please enter Host name")
    }
  }

  def popUpAlert(message: String): Unit = {
    new Alert(AlertType.Warning) {
      initOwner(game.stage)
      title = "Error"
      headerText = message
    }.showAndWait()
  }

  def joinGame(action: ActionEvent): Unit ={
    //reference to client ref
    //game.client ! SentJoin(server.text.value, port.text.value)
    //game.client ! SentJoin("127.0.0.1", "11214", "CLIENT")
    if (join_server_name.text.value != "") {
      if (join_server_ip.text.value != "") {
        if (join_server_port.text.value != "") {
          if (!client_connection_list.getSelectionModel.isEmpty) {
            println("Server IP: " + join_server_ip.text.value + ", Server Port: " + join_server_port.text.value)
            try {
              game.initializeConnect(join_server_name.text.value, client_connection_list.getSelectionModel.getSelectedItem)
              game.client ! SentJoin(join_server_ip.text.value, join_server_port.text.value, join_server_name.text.value, "CLIENT")
              game.enterLobby("CLIENT")
            } catch {
              case e:Exception => println(e)
              //popUpAlert("Unable to connect to server, please try again later")
            }
          } else {
            popUpAlert("Please select connection type")
          }
        } else {
          popUpAlert("Please enter Host Port")
        }
      } else {
        popUpAlert("Please enter Host IP")
      }
    } else {
      popUpAlert("Please enter Host name")
    }
  }
}
