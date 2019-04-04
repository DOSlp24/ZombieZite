package de.htwg.se.zombiezite.model

trait FAreaInterface {
  val x: Int = 0
  val y: Int = 0
  val len: Int
  val wid: Int
  val lines: Vector[Vector[FFieldInterface]]

  def build: FAreaInterface
}
