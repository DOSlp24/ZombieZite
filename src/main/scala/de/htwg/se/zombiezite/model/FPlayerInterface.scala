package de.htwg.se.zombiezite.model

trait FPlayerInterface extends FCharacterInterface {
  val EQMAX = 6
  val equipment: Vector[FItemInterface]

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

  def takeItem(item: FItemInterface): FPlayerInterface

  def equipWeapon(weapon: FWeaponInterface): FPlayerInterface

  def unequipWeapon(): FPlayerInterface

  def useArmor(a: FArmorInterface): FPlayerInterface

  def drop(item: FItemInterface): FPlayerInterface

  def gotInventorySpace(): Boolean

  def burnActionCounter(): FPlayerInterface

  def pWait(): FPlayerInterface

  def resetActionCounter: FPlayerInterface

  override def takeDmg(dmg: Int): FPlayerInterface

  override def walk(x: Int, y: Int): FPlayerInterface
}
