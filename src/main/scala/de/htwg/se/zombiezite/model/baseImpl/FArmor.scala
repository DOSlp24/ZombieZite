package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.FArmorInterface

case class FArmor(name: String, protect: Integer) extends FArmorInterface {
  override val protection: Int = protect
}