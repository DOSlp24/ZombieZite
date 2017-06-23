package de.htwg.se.zombiezite.model.baseImpl

case class Armor(name: String, prot: Integer) extends Item {
  protection = prot
}