package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._
import slick.driver.H2Driver.api._

case class FField(
    override val p: PositionInterface,
    override val chars: Vector[FCharacterInterface] = Vector[FCharacterInterface](),
    override val zombies: Vector[FZombieInterface] = Vector[FZombieInterface](),
    override val players: Vector[FPlayerInterface] = Vector[FPlayerInterface](),
    override val charCount: Int = 0,
    override val noiseCounter: Int = 0
) extends FFieldInterface {

  override def enterPlayer(p: FPlayerInterface): FFieldInterface = {
    val newChars: Vector[FCharacterInterface] = chars :+ p
    val newPlayers: Vector[FPlayerInterface] = players :+ p
    copy(chars = newChars, players = newPlayers, charCount = newChars.length)
  }

  override def enterZombie(z: FZombieInterface): FFieldInterface = {
    val newChars: Vector[FCharacterInterface] = chars :+ z
    val newZombies: Vector[FZombieInterface] = zombies :+ z
    copy(chars = newChars, zombies = newZombies, charCount = newChars.length)
  }

  override def leavePlayer(p: FPlayerInterface): FFieldInterface = {
    val newChars: Vector[FCharacterInterface] = chars.filter(c => c.name != p.name)
    val newPlayers: Vector[FPlayerInterface] = players.filter(player => player.name != p.name)
    copy(chars = newChars, players = newPlayers, charCount = newChars.length)
  }

  override def leaveZombies(z: FZombieInterface): FFieldInterface = {
    val newChars: Vector[FCharacterInterface] = dropFirstMatch[FCharacterInterface](chars, z)
    val newZombies: Vector[FZombieInterface] = dropFirstMatch[FZombieInterface](zombies, z)
    copy(chars = newChars, zombies = newZombies, charCount = newChars.length)
  }

  def dropFirstMatch[A](ls: Vector[A], value: A): Vector[A] = {
    val index = ls.indexOf(value) //index is -1 if there is no match
    if (index < 0) {
      ls
    } else if (index == 0) {
      ls.tail
    } else {
      // splitAt keeps the matching element in the second group
      val (a, b) = ls.splitAt(index)
      a ++ b.tail
    }
  }
}

class FieldTable(tag: Tag) extends Table[(Int, Int, Int, Int, Int)](tag, "Field") {
  def id = column[Int]("ID", O.PrimaryKey)

  def x = column[Int]("X")

  def y = column[Int]("Y")

  def areaID = column[Int]("F_Area")

  def charCount = column[Int]("CharCount")

  def * = (id, x, y, areaID, charCount)

  def area = foreignKey("MY_AREA", areaID, TableQuery[AreaTable])(_.id)
}
