package de.htwg.se.zombiezite.model

trait FWeaponInterface extends FItemInterface {
  val strength: Int
  val range: Int
  val aoe: Int
}