package de.htwg.se.zombiezite.model

trait FCharacterInterface {
  val lifePoints: Int
  val x: Int
  val y: Int
  val strength: Int
  val range: Int
  val equippedWeapon: FWeaponInterface
  val CRITCHANCE = 20
  val armor: Int
  val actionCounter: Int
  val name: String

  def walk(x: Int, y: Int): FCharacterInterface
}
