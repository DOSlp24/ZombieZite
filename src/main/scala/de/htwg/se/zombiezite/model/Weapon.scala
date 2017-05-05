package de.htwg.se.zombiezite.model

case class Weapon(name: String, str: Integer, r: Integer) extends Item{
  strength = str
  range = r
}