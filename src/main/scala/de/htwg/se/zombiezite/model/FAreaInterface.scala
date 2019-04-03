package de.htwg.se.zombiezite.model

trait FAreaInterface {
  val x: Int = 0
  val y: Int = 0
  val laenge: Int
  val breite: Int
  val line: Vector[Vector[FieldInterface]]
}
