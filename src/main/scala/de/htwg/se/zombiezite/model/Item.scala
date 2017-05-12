package de.htwg.se.zombiezite.model

trait Item {
  val name: String
  var strength: Int = 0
  var range: Int = 0
  var aoe = 0
}