package de.htwg.se.zombiezite.model
import scala.collection.mutable.ArrayBuffer

case class Field(p: Position) {
  val length: Int = 2
  var chars = ArrayBuffer[Character]()
  var zombies = ArrayBuffer[Zombie]()
  var players = ArrayBuffer[Player]()
  var charCount: Int = 0
  var noiseCounter: Int = 0
}