class Card (val colour: Char, val attribute: Char){
  def print:Unit={
    println(s"$colour + $attribute")
  }
}