package de.htwg.se.zombiezite.model

trait FPlayerInterface extends FCharacterInterface {
  val EQMAX = 5
  val equipment: Array[FItemInterface]

  def equip(item: FItemInterface): FPlayerInterface = {
    item match {
      case the_item: FWeaponInterface =>
        equipWeapon(the_item)
      case the_item: FArmorInterface =>
        useArmor(the_item)
      case _ =>
        this
    }
  }

  def equipWeapon(weapon: FWeaponInterface): FPlayerInterface

  def useArmor(a: FArmorInterface): FPlayerInterface

  def drop(item: FItemInterface): FPlayerInterface

  override def takeDmg(dmg: Int): FPlayerInterface

  override def walk(x: Int, y: Int): FPlayerInterface
}
