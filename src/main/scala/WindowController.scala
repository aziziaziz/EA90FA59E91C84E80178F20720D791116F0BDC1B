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
class WindowController() {

  def playCard(c: Card): Unit={
    //if cant play card
    //label = wtf m8
    //else
    //discard = card
    //if special card
    //do special stuff
  }
}
