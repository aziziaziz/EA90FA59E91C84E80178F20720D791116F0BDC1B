import scalafx.scene.Group
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{AnchorPane, FlowPane, Pane}
import scalafxml.core.macros.sfxml

import scala.util.Random

@sfxml
class PlayWindowController(
                            player1_play_area: AnchorPane,
                            player2_play_area: AnchorPane,
                            player3_play_area: AnchorPane,
                            player4_play_area: AnchorPane,
                            player1_play_area_card: AnchorPane,
                            player2_play_area_card: AnchorPane,
                            player3_play_area_card: AnchorPane,
                            player4_play_area_card: AnchorPane,
                            add_cards: Button,
                            draw_pile: AnchorPane,
                            tert: Label,
                            pert: Label,
                            pp: Label,
                            player3_play_area_card_flow: FlowPane
                          ) {
  var cardTemplate: Pane =_
  var imageTemplate: ImageView =_
  var originPoint = 0
  val lrt: Array[Group] = new Array[Group](15)

  def userAddCard(): Unit = {
    val ran = Random.nextInt(10)
    val c = Array("b", "g", "r", "y")
    val d = Random.nextInt(4)
    val e = c(d)

    var cardGroup: Group = new Group()

    cardTemplate = new Pane {
      prefHeight = 120.0
      prefWidth = 74
      style = "-fx-border-color: silver; -fx-border-radius: 5;"
    }

    imageTemplate = new ImageView {
      fitHeight = 118.0
      fitWidth = 72.0
      layoutX = 1.0
      layoutY = 1.0
    }

    imageTemplate.image = (new Image(s"cards/$e$ran.png"))

    cardGroup = new Group()
    cardGroup.getChildren.add(cardTemplate)
    cardGroup.getChildren.add(imageTemplate)
    cardGroup.id = s"$e$ran"
    lrt(0) = cardGroup

    cardGroup.layoutY = 0
    cardGroup.layoutX = originPoint

    cardGroup.onMouseEntered = { _ =>
      cardGroup.layoutY = -40
      cardTemplate.style = "-fx-border-color: red; -fx-border-radius: 5;"
    }

    cardGroup.onMouseExited = { _ =>
      cardGroup.layoutY = 0
      cardTemplate.style = "-fx-border-color: silver; -fx-border-radius: 5;"
    }

    /*
    cardGroup.onMouseClicked = { _ =>
      cardTemplate.style = "-fx-border-color: green; -fx-border-radius: 5;"
      tert.text = cardGroup.getId.toString
      pert.text = player1_play_area_card.getChildren.size().toString
      originPoint = cardGroup.layoutX.toInt
      pert.text = player1_play_area_card.getChildren().indexOf(cardGroup).toString

      player1_play_area_card.getChildren.remove(cardGroup)
      reOrganize(cardGroup.layoutX.toDouble, player1_play_area_card.getChildren().indexOf(cardGroup))
      //pert.text = player1_play_area_card.getChildren.size().toString
    }
    */

    cardGroup.onMouseClicked = { _ =>
      player3_play_area_card_flow.getChildren.remove(cardGroup)
      pert.text = cardGroup.getId.toString
    }

    player3_play_area_card_flow.hgap = -50
    player3_play_area_card_flow.vgap = -70
    player3_play_area_card_flow.getChildren.add(cardGroup)
    //player1_play_area_card.getChildren.add(cardGroup)
    //originPoint += 20
    //player1_play_area_card_flow.hgap = -50
    //player1_play_area_card_flow.vgap = -70
    //player1_play_area_card_flow.getChildren.add(cardGroup)
    tert.text = player3_play_area_card_flow.getChildren.size().toString
  }

  def reOrganize(origin: Double, index: Int): Unit = {
    for (a <- index until player1_play_area_card.getChildren.size()) {
      player1_play_area_card.getChildren.get(a).setLayoutX(origin)
      originPoint += 20
    }
  }

}
