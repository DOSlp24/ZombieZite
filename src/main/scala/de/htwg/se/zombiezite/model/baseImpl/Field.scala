package de.htwg.se.zombiezite.model.baseImpl

import scala.collection.mutable.ArrayBuffer
import de.htwg.se.zombiezite.model.{ Character, FieldInterface, ZombieInterface, PlayerInterface }

case class Field(p: Position) extends FieldInterface {
  chars = ArrayBuffer[Character]()
  zombies = ArrayBuffer[ZombieInterface]()
  players = ArrayBuffer[PlayerInterface]()
}