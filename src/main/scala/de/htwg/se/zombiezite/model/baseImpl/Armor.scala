package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.ArmorInterface

case class Armor(name: String, prot: Integer) extends ArmorInterface {
  protection = prot
}