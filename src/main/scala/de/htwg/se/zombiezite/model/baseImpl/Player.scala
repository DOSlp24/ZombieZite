package de.htwg.se.zombiezite.model.baseImpl

import scala.collection.mutable.ArrayBuffer
import de.htwg.se.zombiezite.model.{PlayerInterface, Item, Character, FieldInterface, AreaInterface, ArmorInterface}

case class Player(area: AreaInterface, name: String) extends PlayerInterface {
  var lifePoints: Int = 100
  val strength: Int = 0
  val range: Int = 0
  equipment = ArrayBuffer[Item]()

  def die(): String = {
    leaveField()
    return "AAAAAAAAAAAAAHHHHHHHHHHHHHH!!! ICH STERBE!!!!! \n*Er ist jetzt tot*"
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

  def useArmor(a: ArmorInterface): Boolean = {
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