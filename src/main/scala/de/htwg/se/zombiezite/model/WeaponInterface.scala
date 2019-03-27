package de.htwg.se.zombiezite.model

trait WeaponInterface extends Item {
  var strength: Int = 0
  var range: Int = 0
  var aoe = 0
}