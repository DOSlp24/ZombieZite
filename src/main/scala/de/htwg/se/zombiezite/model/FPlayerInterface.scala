package de.htwg.se.zombiezite.model

trait FPlayerInterface extends FCharacterInterface {
  val EQMAX = 5
  val equipment: Array[Item]

  def equip(item: Item): FPlayerInterface = {
    item match {
      case interface: WeaponInterface =>
        equipWeapon(interface)
      case interface: ArmorInterface =>
        useArmor(interface)
      case _ =>
        this
    }
  }

  def equipWeapon(weapon: WeaponInterface): FPlayerInterface

  def useArmor(a: ArmorInterface): FPlayerInterface

  def drop(item: Item): FPlayerInterface
}
