package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{AreaInterface, FieldInterface}

case class Area(breite: Int, laenge: Int) extends AreaInterface{
  line = Array.ofDim[FieldInterface](laenge, breite)
  val feld: Field = Field(Position(0, 0))
  val step: Int = feld.length

  for (i <- 0 to laenge - 1) {
    for (j <- 0 to breite - 1) {
      line(j)(i) = Field(Position(x + step * j, y + step * i))
    }
  }
}