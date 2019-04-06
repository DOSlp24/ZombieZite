package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

case class FZombie(
                    override val lifePoints: Int,
                    override val x: Int,
                    override val y: Int,
                    override val strength: Int,
                    override val range: Int = 0,
                    override val equippedWeapon: FWeaponInterface = FWeapon("Fist", 1, 0),
                    override val armor: Int = 0,
                    override val actionCounter: Int = 1,
                    override val name: String,
                    override val archenemy: FPlayerInterface = FPlayerWithoutIdentity()
                  ) extends FZombieInterface {

  override def walk(xInc: Int, yInc: Int): FZombieInterface = {
    copy(x = x + xInc, y = y + yInc)
  }

  override def takeDmg(dmg: Int): FZombieInterface = {
    val armorLeft = armor - dmg
    if (armorLeft >= 0) {
      copy(armor = armorLeft)
    } else {
      copy(armor = 0, lifePoints = lifePoints + armorLeft)
    }
  }

  override def selectTarget(p: FPlayerInterface): FZombieInterface = {
    copy(archenemy = p)
  }
}
