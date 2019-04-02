package de.htwg.se.zombiezite.model

trait FCharacterInterface {
  val lifePoints: Int
  val strength: Int
  val range: Int
  val actualField: FieldInterface
  val equipedWeapon: WeaponInterface
  val kritchance = 20
  var armor: Int
  val area: AreaInterface
  var actionCounter: Int
  val name: String

  def walk(): FCharacterInterface
}
