package de.htwg.se.zombiezite.model
import scala.collection.mutable.ArrayBuffer

case class Player(area: Area, name: String) extends Character {
  val EQMAX = 6
  var lifePoints: Int = 100
  val strength: Int = 0
  val range: Int = 0
  var actualField: Field = null
  var equipment = ArrayBuffer[Item]()

  def die(): Unit = {
    println("AAAAAAAAAAAAAHHHHHHHHHHHHHH!!! ICH STERBE!!!!! \n*Er ist jetzt tot*")
  }

  def leaveField() {
    actualField.players.remove(actualField.players.indexOf(this))
    actualField.chars.remove(actualField.chars.indexOf(this))
  }

  def enterField() {
    actualField.players.append(this)
    actualField.chars.append(this)
  }

  def equip(item: Item): Boolean = {
    if (equipment.length <= EQMAX) {
      equipment.append(item)
      return true
    }
    return false
  }

  def drop(item: Item): Item = {
    for (i <- 0 to equipment.length - 1) {
      if (equipment(i).name == item.name) {
        val tmp =  equipment(i)
        equipment.remove(i)
        return tmp
      }
    }
    return null
  }
  
  def printEq(){
     for (i <- 0 to equipment.length - 1) {
       println("["+i+"] " + equipment(i).name)
     }
  }
}