package de.htwg.se.zombiezite.model

trait FCharacterInterface {
  val lifePoints: Int
  val x: Int
  val y: Int
  val strength: Int
  val range: Int
  val actualField: FieldInterface
  val equippedWeapon: WeaponInterface
  val kritchance = 20
  var armor: Int
  val area: AreaInterface
  var actionCounter: Int
  val name: String

  def walk(x: Int, y: Int): FCharacterInterface
}
