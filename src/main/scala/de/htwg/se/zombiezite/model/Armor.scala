package de.htwg.se.zombiezite.model

case class Armor(name: String, prot: Integer) extends Item {
  protection = prot
}