package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.FWeaponInterface
import slick.driver.H2Driver.api._

//it is not required to pass the aoe value to each weapon. Default = 0
case class FWeapon(name: String, str: Integer, ran: Integer, ao: Int = 0) extends FWeaponInterface {
  override val strength: Int = str
  override val range: Int = ran
  override val aoe: Int = ao
}

class WeaponTable(tag: Tag) extends Table[(Int, String, Boolean, String, Int, Int, Int)](tag, "Weapon") {
  def id = column[Int]("ID", O.PrimaryKey)

  def belongsTo = column[String]("Owner")

  def isEquipped = column[Boolean]("IsEquipped")

  def name = column[String]("Name")

  def str = column[Int]("Strength")

  def ran = column[Int]("Range")

  def aoe = column[Int]("AOE")

  def * = (id, belongsTo, isEquipped, name, str, ran, aoe)

  def owner = foreignKey("MY_OWNER_WEAPOM", belongsTo, TableQuery[PlayerTable])(_.name)
}