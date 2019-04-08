package de.htwg.se.zombiezite.model

trait FPlayerInterface extends FCharacterInterface {
  val EQMAX = 5
  val equipment: Array[FItemInterface]

  def equip(item: Item): FPlayerInterface = {
    item match {
      case interface: FWeaponInterface =>
        equipWeapon(interface)
      case interface: FArmorInterface =>
        useArmor(interface)
      case _ =>
        this
    }
  }

  def equipWeapon(weapon: FWeaponInterface): FPlayerInterface

  def useArmor(a: FArmorInterface): FPlayerInterface

  def drop(item: FItemInterface): FPlayerInterface

  def gotInventorySpace(): Boolean

  def burnActionCounter(): FPlayerInterface

  def pWait(): FPlayerInterface

  def resetActionCounter: FPlayerInterface

  override def takeDmg(dmg: Int): FPlayerInterface

  override def walk(x: Int, y: Int): FPlayerInterface
}
