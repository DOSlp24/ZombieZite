package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

case class FPlayer(
  override val lifePoints: Int,
    override val x: Int,
    override val y: Int,
    override val armor: Int,
    override val strength: Int,
    override val range: Int,
    override val actualField: FieldInterface,
    override val area: AreaInterface,
    override val name: String,
    override val actionCounter: Int,
    override val equipment: Array[Item],
    override val equippedWeapon: WeaponInterface = new Weapon("Fist", 1, 0)
) extends FPlayerInterface {

  override def useArmor(a: ArmorInterface): FPlayerInterface = {
    copy(armor = armor + a.protection)
  }

  override def equipWeapon(weapon: WeaponInterface): FPlayerInterface = {
    copy(equippedWeapon = weapon)
  }

  override def drop(item: Item): FPlayerInterface = {
    val eq = equipment.filter(_ != item)
    copy(equipment = eq)
  }

  override def walk(xInc: Int, yInc: Int): FCharacterInterface = {
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
