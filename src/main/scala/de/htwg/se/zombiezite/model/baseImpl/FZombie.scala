package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

case class FZombie(
    override val lifePoints: Int,
    override val x: Int,
    override val y: Int,
    override val strength: Int,
    override val range: Int = 0,
    override val actualField: FFieldInterface,
    override val equippedWeapon: FWeaponInterface = FWeapon("Fist", 1, 0),
    override val armor: Int = 0,
    override val area: FAreaInterface,
    override val actionCounter: Int = 1,
    override val name: String
) extends FZombieInterface {

  override def walk(xInc: Int, yInc: Int): FZombie = {
    if (this.x + xInc < 0
      || this.x + xInc > this.area.breite
      || this.y + yInc < 0
      || this.y + yInc > this.area.laenge) {
      this
    } else {
      copy(x = x + xInc, y = y + yInc)
    }
  }
}
