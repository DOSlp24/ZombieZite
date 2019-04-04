package de.htwg.se.zombiezite.model

trait FWeaponInterface extends Item {
  val strength: Int
  val range: Int
  val aoe: Int
}