package de.htwg.se.zombiezite.model

trait FCharacterInterface {
  val lifePoints: Int
  val x: Int
  val y: Int
  val strength: Int
  val range: Int
  val actualField: FFieldInterface
  val equippedWeapon: FWeaponInterface
  val CRITCHANCE = 20
  val armor: Int
  val area: FAreaInterface
  val actionCounter: Int
  val name: String

  def walk(x: Int, y: Int): FCharacterInterface
}
