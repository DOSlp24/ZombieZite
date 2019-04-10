package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{ FArmorInterface, FItemInterface, FPlayerInterface, FWeaponInterface }

case class FPlayerWithoutIdentity() extends FPlayerInterface {
  override val equipment: Vector[FItemInterface] = Vector[FItemInterface]()

  override def equipWeapon(weapon: FWeaponInterface): FPlayerInterface = {
    this
  }

  override def useArmor(a: FArmorInterface): FPlayerInterface = {
    this
  }

  override def drop(item: FItemInterface): FPlayerInterface = {
    this
  }

  override def takeDmg(dmg: Int): FPlayerInterface = {
    this
  }

  override def walk(x: Int, y: Int): FPlayerInterface = {
    this
  }

  override val lifePoints: Int = 100
  override val x: Int = 0
  override val y: Int = 0
  override val strength: Int = 0
  override val range: Int = 0
  override val equippedWeapon: FWeaponInterface = FWeapon("", 0, 0)
  override val armor: Int = 0
  override val actionCounter: Int = 0
  override val name: String = ""

  override def gotInventorySpace(): Boolean = true

  override def burnActionCounter(): FPlayerInterface = this

  override def pWait(): FPlayerInterface = this

  override def resetActionCounter: FPlayerInterface = this

  override def takeItem(item: FItemInterface): FPlayerInterface = this

  override def unequipWeapon(): FPlayerInterface = this
}
