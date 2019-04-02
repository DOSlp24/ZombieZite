package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

case class Zombie(area: AreaInterface, name: String, str: Int, ran: Int, lp: Int) extends ZombieInterface {
  var lifePoints: Int = lp
  val strength: Int = str
  val range: Int = ran
  var targetField: FieldInterface = actualField
  var distanceToTargetField = 1000

  def die(): String = {
    leaveField()
    return "Ein " + name + " weniger"
  }

  def leaveField() {
    if (actualField.zombies.contains(this) && actualField.chars.contains(this)) {
      actualField.zombies.remove(actualField.zombies.indexOf(this))
      actualField.chars.remove(actualField.chars.indexOf(this))
    }
  }

  def enterField() {
    actualField.zombies.append(this)
    actualField.chars.append(this)
  }
}