package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{FArmorInterface, FItemInterface, FPlayerInterface, FWeaponInterface}

case class FPlayerWithoutIdentity() extends FPlayerInterface{
  override val equipment: Array[FItemInterface] = _

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

  override val lifePoints: Int = _
  override val x: Int = _
  override val y: Int = _
  override val strength: Int = _
  override val range: Int = _
  override val equippedWeapon: FWeaponInterface = _
  override val armor: Int = _
  override val actionCounter: Int = _
  override val name: String = _
}
