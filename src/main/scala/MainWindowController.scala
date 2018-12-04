import Client.SentJoin
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.image._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Pane
import scalafxml.core.macros.sfxml
import scalafx.Includes._

@sfxml
class MainWindowController(
                            private val image_banner: ImageView,
                            private val image_pane: Pane,
                            private val create_game: Button,
                            private val join_game: Button,
                            private val host_name: TextField) {
  val image = new Image("images/uno.png")

  image_banner.image_=(image)

  def enter(some: Button): Unit = {
    some.setStyle("-fx-border-color:red; -fx-background-color: red; -fx-border-width: 2; -fx-border-radius: 5; -fx-font-weight: bold; -fx-font-color: white;")
  }

  def leave(some: Button): Unit = {
    some.setStyle("-fx-border-color:silver; -fx-background-color: none; -fx-border-width: 2; -fx-border-radius: 5; -fx-font-weight: bold; -fx-font-color: #6b6b6b;")
  }
  create_game.text = host_name.text()
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

  def hostGame(action: ActionEvent): Unit ={
    //reference to client ref
    //MyPassBall.client ! SentJoin(server.text.value, port.text.value)
    game.client ! SentJoin("127.0.0.1", "11214", "HOST")
  }

  def joinGame(action: ActionEvent): Unit ={
    //reference to client ref
    //game.client ! SentJoin(server.text.value, port.text.value)
    //game.client ! SentJoin("127.0.0.1", "11214", "CLIENT")
  }

  join_game.onMouseEntered = { _ =>
    enter(join_game)
  }

  join_game.onMouseExited = { _ =>
    leave(join_game)
  }

  create_game.onMouseClicked = { _ =>
    game.client ! SentJoin("127.0.0.1", host_name.text.value, "CLIENT")
    game.enterLobby()
  }
}
