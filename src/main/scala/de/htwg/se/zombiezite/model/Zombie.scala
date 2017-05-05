package de.htwg.se.zombiezite.model

case class Zombie(area: Area, typ: String, str: Int, ran: Int, lp: Int) extends Character {
  var lifePoints: Int = lp
  val strength: Int = str
  val range: Int = ran
  var actualField: Field = null
  var targetField: Field = actualField
  var distanceToTargetField = 1000

  def die(): Unit = {
    println("Ein " + typ + " weniger")
  }

  def leaveField() {
    actualField.zombies.remove(actualField.zombies.indexOf(this))
    actualField.chars.remove(actualField.chars.indexOf(this))
  }

  def enterField() {
    actualField.zombies.append(this)
    actualField.chars.append(this)
  }
}