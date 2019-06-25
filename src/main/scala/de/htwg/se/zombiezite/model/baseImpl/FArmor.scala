package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.FArmorInterface
import slick.driver.H2Driver.api._

case class FArmor(name: String, protect: Integer) extends FArmorInterface {
  override val protection: Int = protect
}

class ArmorTable(tag: Tag) extends Table[(Int, String, String, Int)](tag, "Armor") {
  def id = column[Int]("ID", O.PrimaryKey)

  def ownerID = column[String]("Owner")

  def name = column[String]("Name")

  def protection = column[Int]("Protection")

  def * = (id, ownerID, name, protection)

  def owner = foreignKey("MY_OWNER_ARMOR", ownerID, TableQuery[PlayerTable])(_.name)
}