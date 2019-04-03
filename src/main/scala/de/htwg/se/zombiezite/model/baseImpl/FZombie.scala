package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

case class FZombie(
    override val lifePoints: Int,
    override val x: Int,
    override val y: Int,
    override val strength: Int,
    override val range: Int,
    override val actualField: FieldInterface,
    override val equippedWeapon: WeaponInterface,
    override var armor: Int,
    override val area: AreaInterface,
    override var actionCounter: Int,
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
