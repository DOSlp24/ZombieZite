package de.htwg.se.zombiezite.model

import scala.collection.mutable.ArrayBuffer

trait FieldInterface {
  val length: Int = 2
  val p: PositionInterface
  var chars: ArrayBuffer[Character] = null
  var zombies: ArrayBuffer[ZombieInterface] = null
  var players: ArrayBuffer[PlayerInterface] = null
  var charCount: Int = 0
  var noiseCounter: Int = 0
}