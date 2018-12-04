
import akka.actor.ActorRef
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, ListView, TextField}
import scalafx.scene.shape.Circle
import scalafxml.core.macros.sfxml

@sfxml
class WindowController() {

  def playCard(c: Card):String ={
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
}
