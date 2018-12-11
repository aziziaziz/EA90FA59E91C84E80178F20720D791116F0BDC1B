import akka.actor.ActorRef
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, ListView, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.shape.Circle
import scalafxml.core.macros.sfxml

@sfxml
class WindowController(
                        private val server: TextField,
                        private val port: TextField,
                        private val status: Label,
                        private val clientList: ListView[ActorRef],
                        private val ball: Circle,
                        private val btnPass: Button,
                        private val himage: ImageView) {

  game.clients.onChange((x, y) => {
    Platform.runLater {
      clientList.items = new ObservableBuffer() ++= x.toList
    }
  })

  def playCard(c: Card): String = {
    /*if (c.attribute == discard.attribute || c.type == discard.type)
    {
      discard = c
      switch(discard.type)
        case '+' : return "draw"
                    break
        case 'c' : return "change"
                    break
        default : return "normie"
    }
    */
    return "wrong"


  }
  /*
  def hideBall(): Unit ={
    ball.visible = false
    btnPass.disable = true
  }
  def showBall(): Unit ={
    ball.visible = true
    btnPass.disable = false
  }

  def handleJoin(action: ActionEvent): Unit ={
    //reference to client ref
    //game.client ! SentJoin(server.text.value, port.text.value, "", "")
  }

  def handlePass(action: ActionEvent): Unit ={
    //game.client ! PassBall
  }

  def handleStart(action: ActionEvent): Unit ={
    //game.server ! Start
  }
  def displayStatus(data: String): Unit = {
    status.text = data
  }

  def Statusu(data: String) = {
    status.text = data
  }
  */
}
