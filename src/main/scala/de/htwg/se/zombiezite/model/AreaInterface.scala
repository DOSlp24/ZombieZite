package de.htwg.se.zombiezite.model

trait AreaInterface {
  val x: Int = 0
  val y: Int = 0
  val laenge: Int
  val breite: Int
  var line: Array[Array[FieldInterface]] = null
}