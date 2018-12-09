import scalafx.scene.Group
import scalafx.scene.control.{Button, Label, ScrollPane}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafxml.core.macros.sfxml

import scala.util.Random
import scalafx.Includes._
import scalafx.scene.effect.ColorAdjust

@sfxml
class PlayWindowController (
                            player1_play_area: AnchorPane,
                            player2_play_area: AnchorPane,
                            player3_play_area: AnchorPane,
                            player4_play_area: AnchorPane,
                            player1_play_area_card: AnchorPane,
                            player2_play_area_card: AnchorPane,
                            player3_play_area_card: AnchorPane,
                            player4_play_area_card: AnchorPane,
                            add_cards: Button,
                            tert: Label,
                            pert: Label,
                            pp: Label,
                            player3_play_area_card_flow: HBox,
                            player1_play_area_card_flow: HBox,
                            player2_play_area_card_flow: VBox,
                            player4_play_area_card_flow: VBox,
                            draw_pile: Pane,
                            discard_pile: Pane,
                            dummy_cards: Button,
                            discard_pile_image: ImageView,
                            hboxed: HBox,
                            btrs: Button,
                            player2_scroll: ScrollPane,
                            player1_scroll: ScrollPane
                          ) {
  var cardTemplate: Pane =_
  var imageTemplate: ImageView =_
  var originPoint = 0
  val lrt: Array[Group] = new Array[Group](15)
  val colorAdjust: ColorAdjust = new ColorAdjust()

  /*
  init() - setting up the whole game, add cards to user hand setting up the initial discard pile card
  victoryCondition() - checks everytime usser plays a card, if hand == 0 then win and broadcast win condition
                       to server
  loosingCondition() - same as before, checks everytime user draws a card or being forced to draw cards, hand > 15 loose
  userAddCard() - function is run when user draw a card from the draw deck or being forced to draw cards
  discardCard() - user plays card into discard pile
  turn() - ends turn when user plays card, disable user hand
  addDummy() - add cards to other players hand

  to be implemented
  Skip and reverse
   */

  def init(): Unit = {
    player1_scroll.setStyle("-fx-focus-color: transparent; -fx-background-color:transparent;")
    addDummy()

    for(i <- 1 to 10) {
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

      imageTemplate.image = new Image(s"cards/$e$ran.png")

      cardGroup.getChildren.add(cardTemplate)
      cardGroup.getChildren.add(imageTemplate)
      cardGroup.id = s"$e$ran"

      cardGroup.onMouseEntered = { _ =>
        cardGroup.translateY = -18
      }

      cardGroup.onMouseExited = { _ =>
        cardGroup.translateY = 0
      }

      cardGroup.onMouseClicked = { _ =>
        player1_play_area_card_flow.getChildren.remove(cardGroup)
        pert.text = cardGroup.getId.toString
        discardCard(cardGroup.getId.toString)
      }

      player1_play_area_card_flow.spacing = -40
      player1_play_area_card_flow.getChildren.add(cardGroup)
      tert.text = player3_play_area_card_flow.getChildren.size().toString
    }
    limitCards()
  }

  init()

  def victoryCondition(): Unit = {

  }

  def loosingCondition(): Unit = {

  }

  def btr(): Unit = {
    player2_play_area_card_flow.getChildren.remove(0)
    player3_play_area_card_flow.getChildren.remove(0)
    player4_play_area_card_flow.getChildren.remove(0)
  }

  def userAddCard(): Unit = {
    player1_scroll.setStyle("-fx-focus-color: transparent;")

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

    imageTemplate.image = new Image(s"cards/$e$ran.png")

    cardGroup.getChildren.add(cardTemplate)
    cardGroup.getChildren.add(imageTemplate)
    cardGroup.id = s"$e$ran"
    //lrt(0) = cardGroup

    //cardGroup.layoutY = 0
    //cardGroup.layoutX = originPoint

    cardGroup.onMouseEntered = { _ =>
      cardGroup.translateY = -18
    }

    cardGroup.onMouseExited = { _ =>
      cardGroup.translateY = 0
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
      player1_play_area_card_flow.getChildren.remove(cardGroup)
      pert.text = cardGroup.getId.toString
      discardCard(cardGroup.getId.toString)
    }

    //player3_play_area_card_flow.hgap = -50
    //player3_play_area_card_flow.vgap = -70
    //player3_play_area_card_flow.getChildren.add(cardGroup)
    //player1_play_area_card.getChildren.add(cardGroup)
    //originPoint += 20
    //player1_play_area_card_flow.hgap = -40
    //player1_play_area_card_flow.vgap = -70
    player1_play_area_card_flow.spacing = -40
    player1_play_area_card_flow.getChildren.add(cardGroup)
    tert.text = player3_play_area_card_flow.getChildren.size().toString
  }

  def discardCard(some: String): Unit = {
    discard_pile_image.image = new Image(s"cards/$some.png")
  }

  def turn(): Unit = {
    player1_play_area_card_flow.disable = true
    player2_scroll.setStyle("-fx-border-color: yellow;")
  }

  def resetContrast(): Unit = {
    colorAdjust.setContrast(0)
    //colorAdjust.setContrast(0)
    player1_play_area_card_flow.getChildren.forEach(child => {
      child.disable = false
      child.setEffect(colorAdjust)
    })
  }

  def limitCards(): Unit = {
    colorAdjust.setContrast(-0.5)
    //colorAdjust.setContrast(0)
    player1_play_area_card_flow.getChildren.forEach(child => {
      if(!child.getId.equals("r4")) {
        child.disable = true
        child.setEffect(colorAdjust)
      }
    })
  }

  def addDummy(): Unit = {
    for(i <- 1 to 10) {
      var temp = new Group()
      var tempP: Pane = new Pane {
        prefHeight = 120.0
        prefWidth = 74
        style = "-fx-border-color: silver; -fx-border-radius: 5;"
      }

      var imV: ImageView = new ImageView {
        fitHeight = 118.0
        fitWidth = 72.0
        layoutX = 1.0
        layoutY = 1.0
      }

      imV.image = new Image(s"cards/ub.png")

      temp.getChildren.add(tempP)
      temp.getChildren.add(imV)
      temp.rotate = 90

      player2_play_area_card_flow.setSpacing(-90)
      player2_play_area_card_flow.getChildren.add(temp)
    }

    for(i <- 1 to 10) {
      var temp = new Group()
      var tempP: Pane = new Pane {
        prefHeight = 120.0
        prefWidth = 74
        style = "-fx-border-color: silver; -fx-border-radius: 5;"
      }

      var imV: ImageView = new ImageView {
        fitHeight = 118.0
        fitWidth = 72.0
        layoutX = 1.0
        layoutY = 1.0
      }

      imV.image = new Image(s"cards/ub.png")

      temp.getChildren.add(tempP)
      temp.getChildren.add(imV)

      player3_play_area_card_flow.spacing = -40
      player3_play_area_card_flow.getChildren.add(temp)
    }

    for(i <- 1 to 10) {
      var temp = new Group()
      var tempP: Pane = new Pane {
        prefHeight = 120.0
        prefWidth = 74
        style = "-fx-border-color: silver; -fx-border-radius: 5;"
      }

      var imV: ImageView = new ImageView {
        fitHeight = 118.0
        fitWidth = 72.0
        layoutX = 1.0
        layoutY = 1.0
      }

      imV.image = (new Image(s"cards/ub.png"))

      temp.getChildren.add(tempP)
      temp.getChildren.add(imV)
      temp.rotate = 90

      player4_play_area_card_flow.setSpacing(-90)
      player4_play_area_card_flow.getChildren.add(temp)
    }
  }

  /*
  def reOrganize(origin: Double, index: Int): Unit = {
    for (a <- index until player1_play_area_card.getChildren.size()) {
      player1_play_area_card.getChildren.get(a).setLayoutX(origin)
      originPoint += 20
    }
  }
  */
}
