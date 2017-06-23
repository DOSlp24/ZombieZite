package de.htwg.se.zombiezite.model
import scala.collection.mutable.ArrayBuffer

case class Player(area: Area, name: String) extends PlayerInterface {
  val EQMAX = 5
  var lifePoints: Int = 100
  val strength: Int = 0
  val range: Int = 0
  var actualField: Field = null
  var equipment = ArrayBuffer[Item]()

  def die(): String = {
    leaveField()
    return"AAAAAAAAAAAAAHHHHHHHHHHHHHH!!! ICH STERBE!!!!! \n*Er ist jetzt tot*"
  }

  def leaveField() {
    if (actualField.players.contains(this) && actualField.chars.contains(this)) {
      actualField.players.remove(actualField.players.indexOf(this))
      actualField.chars.remove(actualField.chars.indexOf(this))
    }
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
  
  def useArmor(a: Item): Boolean = {
    a.name match {
      case "Healkit" => {
        lifePoints = 100
        armor += 10
      }
      case _ => armor += a.protection
    }
    return true
  }

  def drop(item: Item): Item = {
    for (i <- 0 to equipment.length - 1) {
      if (equipment(i).name == item.name) {
        val tmp = equipment(i)
        equipment.remove(i)
        return tmp
      }
    }
    return null
  }
}