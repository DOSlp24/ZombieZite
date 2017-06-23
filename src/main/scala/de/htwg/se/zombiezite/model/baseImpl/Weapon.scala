package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.WeaponInterface

case class Weapon(name: String, str: Integer, r: Integer) extends WeaponInterface {
  strength = str
  range = r
}