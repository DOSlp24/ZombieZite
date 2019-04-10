package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

case class FPlayer(
    override val x: Int,
    override val y: Int,
    override val lifePoints: Int = 100,
    override val armor: Int = 0,
    override val strength: Int = 1,
    override val range: Int = 0,
    override val name: String = "Player",
    override val actionCounter: Int = 3,
    override val equipment: Vector[FItemInterface] = Vector[FItemInterface](),
    override val equippedWeapon: FWeaponInterface = FWeapon("Fist", 1, 0)
) extends FPlayerInterface {

  override def takeItem(item: FItemInterface): FPlayerInterface = {
    if (gotInventorySpace()) {
      copy(equipment = equipment :+ item).burnActionCounter()
    } else {
      this
    }
  }

  override def useArmor(a: FArmorInterface): FPlayerInterface = {
    copy(armor = armor + a.protection).drop(a)
  }

  override def equipWeapon(weapon: FWeaponInterface): FPlayerInterface = {
    copy(equippedWeapon = weapon)
  }

  override def drop(item: FItemInterface): FPlayerInterface = {
    val eq = equipment.filter(_ != item)
    copy(equipment = eq).burnActionCounter()
  }

  override def walk(xInc: Int, yInc: Int): FPlayerInterface = {
    copy(x = x + xInc, y = y + yInc).burnActionCounter()
  }

  override def takeDmg(dmg: Int): FPlayerInterface = {
    val armorLeft = armor - dmg
    if (armorLeft >= 0) {
      copy(armor = armorLeft)
    } else {
      copy(armor = 0, lifePoints = lifePoints + armorLeft)
    }
  }

  override def burnActionCounter(): FPlayerInterface = {
    copy(actionCounter = actionCounter - 1)
  }

  override def pWait(): FPlayerInterface = {
    copy(actionCounter = 0)
  }

  override def resetActionCounter: FPlayerInterface = {
    copy(actionCounter = 3)
  }

  override def gotInventorySpace(): Boolean = {
    equipment.length < EQMAX
  }
}
