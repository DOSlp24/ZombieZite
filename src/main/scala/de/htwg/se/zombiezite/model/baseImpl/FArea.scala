package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{ FAreaInterface, FFieldInterface, FieldInterface }
import slick.driver.H2Driver.api._

case class FArea(
    override val len: Int,
    override val wid: Int,
    override val lines: Vector[Vector[FFieldInterface]] = Vector[Vector[FFieldInterface]]()
) extends FAreaInterface {

  override def build(): FArea = {
    copy(lines = buildLines(0, lines))
  }

  def buildLines(lineNum: Int, myLines: Vector[Vector[FFieldInterface]]): Vector[Vector[FFieldInterface]] = {
    if (lineNum < wid) {
      buildLines(lineNum + 1, myLines :+ buildLine(lineNum, 0))
    } else {
      myLines
    }
  }

  def buildLine(lineNum: Int, fieldNum: Int, myLine: Vector[FFieldInterface] = Vector[FFieldInterface]()): Vector[FFieldInterface] = {
    if (fieldNum < len) {
      buildLine(lineNum, fieldNum + 1) :+ FField(Position(len - 1 - fieldNum, lineNum))
    } else {
      myLine
    }
  }

  override def putField(field: FFieldInterface): FArea = {
    val newLine = lines.apply(field.p.y).updated(field.p.x, field)
    val newLines = lines.updated(field.p.y, newLine)
    copy(lines = newLines)
  }
}

class AreaTable(tag: Tag) extends Table[(Int, Int, Int)](tag, "Area") {
  def id = column[Int]("ID", O.PrimaryKey)

  def len = column[Int]("Length")

  def wid = column[Int]("Width")

  def * = (id, len, wid)
}