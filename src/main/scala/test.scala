import scalafx.collections.ObservableBuffer

import scala.util.Random


object test extends App {
  /*
      //server side
      //deck creation
      var deck = new ObservableBuffer[String]()
      var colour = Array("b", "g", "r", "y")
      for (i <- Range(0, 2)){
        for(j <- colour){
          for(k <- Range(1,10))
            deck += j.toString + k.toString
        }
        for(j <- colour){
          deck += j.toString + "s"
          deck += j.toString + "r"
          deck += j.toString + "+"
        }
      }

      for(i <- colour){
        deck += i.toString + "0"
      }

      for(i <- Range(0, 4)){
        deck += "4+"
        deck += "cc"
      }


      var players = Array("player 1", "player 2", "player 3", "player 4")

      var turn = 0
      var next = 1

      //every turn turn + next
      // player 1 -> player 2
      // turn = 4 then turn = 0 (player 4 -> player 1)
      // reverse played then next = -1
      //player 3 -> player 2
      // turn = -1 then turn = 3 (player 1 -> player 4)
      // reverse again next = 1
      next = if (next == 1) -1 else 1
      //skip
      //if next == 1
      turn = if (turn+2>3)turn-3 else turn + 1
      //normal turn increment still apply
      // if next == -1
      turn = if (turn-2<0) turn+3 else turn -1
      //normal turn increment still apply

      //draw
      // go to next player i.e.  turn + next
      // if has +2/4 card, throw +2/4
      //else draw card
      //then apply skip



//testing codes pay no heed
  //    deck.foreach(x => print(x + " "))

  //    println(deck.length)

      var drawPile = Random.shuffle(deck)

 //     drawPile.foreach(x => print(x + " "))

      var discardPile: String = drawPile.get(Random.nextInt().abs % 108)

      var play = ""

      println("\n" + discardPile)

 //     var count = 0
    //  play = scala.io.StdIn.readLine()

      //client side
      if(play.charAt(0).equals(discardPile.charAt(0)) || play.charAt(1).equals(discardPile.charAt(1)) || play.equals("cc") ||
        play.equals("4+")){
        if(play.charAt(1).toInt > 58 || play.charAt(1) == 43){
          if(play.charAt(1).equals('r')){
            //reverse order
            //send reverse to server
          }else if (play.charAt(1).equals('s')){
            //skip turn
            //send skip to server
          }else if (play.charAt(1).equals('+')){
            //draw card
            println("DURAW")
            if(play.equals("4+")){
              //colour change
              //discardPile = "c+" (c = selected colour)
            }
          } else{
            //colour change
            //discardPile = "c+" (c = selected colour)
            println("aaaaaaaa")
          }
        }else{
          println("normie reeeee")
        }
      }else{
        println("wtf bro")
      }

      //if hand = 0 then win
*/
}

