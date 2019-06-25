package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.FItemInterface
import slick.driver.H2Driver.api._

case class FTrash(override val name: String) extends FItemInterface {
}

class TrashTable(tag: Tag) extends Table[(Int, String, String)](tag, "Trash") {
  def id = column[Int]("ID", O.PrimaryKey)

  def ownerID = column[String]("Owner")

  def name = column[String]("Name")

  def * = (id, ownerID, name)

  def owner = foreignKey("MY_OWNER_TRASH", ownerID, TableQuery[PlayerTable])(_.name)
}
