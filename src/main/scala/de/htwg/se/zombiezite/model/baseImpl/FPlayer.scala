package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._
import slick.driver.H2Driver.api._

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
    override val equippedWeapon: FWeaponInterface = FWeapon("Fists", 1, 0)
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
    unequipWeapon().asInstanceOf[FPlayer].takeWeapon(weapon)
  }

  def takeWeapon(w: FWeaponInterface): FPlayerInterface = {
    copy(equippedWeapon = w).drop(w)
  }

  override def unequipWeapon(): FPlayerInterface = {
    if (gotInventorySpace() && equippedWeapon.name != "Fists") {
      takeItem(equippedWeapon).equipWeapon(FWeapon("Fists", 1, 0))
    } else {
      this
    }
  }

  override def drop(item: FItemInterface): FPlayerInterface = {
    val eq = equipment.filter(_ != item)
    copy(equipment = eq)
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

class PlayerTable(tag: Tag) extends Table[(String, Int, Int, Int, Int, Int)](tag, "Player") {
  def name = column[String]("Name", O.PrimaryKey)

  def fieldId = column[Int]("F_Field")

  def lifepoints = column[Int]("Lifepoints")

  def armor = column[Int]("Armor")

  def stren = column[Int]("Strength")

  def ran = column[Int]("Range")

  def * = (name, fieldId, lifepoints, armor, stren, ran)

  def myField = foreignKey("MY_FIELD_PLAYER", fieldId, TableQuery[FieldTable])(_.id)
}
