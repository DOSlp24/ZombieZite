package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.FWeaponInterface

//it is not required to pass the aoe value to each weapon. Default = 0
case class FWeapon(name: String, str: Integer, ran: Integer, ao: Int = 0) extends FWeaponInterface {
  override val strength: Int = str
  override val range: Int = ran
  override val aoe: Int = ao
}