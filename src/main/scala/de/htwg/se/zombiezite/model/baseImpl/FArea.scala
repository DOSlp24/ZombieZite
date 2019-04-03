package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{ FAreaInterface, FieldInterface }

case class FArea(
    override val laenge: Int,
    override val breite: Int,
    override val line: Vector[Vector[FieldInterface]]
) extends FAreaInterface {
}