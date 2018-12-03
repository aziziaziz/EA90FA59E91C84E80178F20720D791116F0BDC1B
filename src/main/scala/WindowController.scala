import Client.{PassBall, SentJoin}
import Server.Start
import akka.actor.ActorRef

import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, ListView, TextField}
import scalafx.scene.shape.Circle
import scalafxml.core.macros.sfxml

@sfxml
class WindowController(
                        private val server: TextField,
                        private val port: TextField,
                        private val status: Label,
                        private val clientList: ListView[ActorRef],
                        private val ball: Circle,
                        private val btnPass: Button) {
  MyPassBall.clients.onChange((x, y) => {
    Platform.runLater {
      clientList.items = new ObservableBuffer() ++= x.toList
    }
  })

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
    MyPassBall.client ! SentJoin(server.text.value, port.text.value)
  }

  def handlePass(action: ActionEvent): Unit ={
    MyPassBall.client ! PassBall
  }

  def handleStart(action: ActionEvent): Unit ={
    MyPassBall.server ! Start
  }
  def displayStatus(data: String): Unit = {
    status.text = data
  }
}
