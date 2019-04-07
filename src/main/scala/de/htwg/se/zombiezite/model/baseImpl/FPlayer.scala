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
    override val equipment: Array[FItemInterface] = Array[FItemInterface](),
    override val equippedWeapon: FWeaponInterface = FWeapon("Fist", 1, 0)
) extends FPlayerInterface {

  override def useArmor(a: FArmorInterface): FPlayerInterface = {
    copy(armor = armor + a.protection)
  }

  override def equipWeapon(weapon: FWeaponInterface): FPlayerInterface = {
    copy(equippedWeapon = weapon)
  }

  override def drop(item: FItemInterface): FPlayerInterface = {
    val eq = equipment.filter(_ != item)
    copy(equipment = eq)
  }

  override def walk(xInc: Int, yInc: Int): FPlayerInterface = {
    copy(x = x + xInc, y = y + yInc)
  }

  override def takeDmg(dmg: Int): FPlayerInterface = {
    val armorLeft = armor - dmg
    if (armorLeft >= 0) {
      copy(armor = armorLeft)
    } else {
      copy(armor = 0, lifePoints = lifePoints + armorLeft)
    }
  }
}
