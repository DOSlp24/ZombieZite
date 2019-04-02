package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

class FPlayer(override val lifePoints: Int,
              override val armor: Int,
              override val strength: Int,
              override val range: Int,
              override val actualField: FieldInterface,
              override val area: AreaInterface,
              override val name: String,
              override val actionCounter: Int,
              override val equipedWeapon: WeaponInterface = new Weapon("Fist", 1, 0)) extends FPlayerInterface {

  override def useArmor(a: ArmorInterface): FPlayerInterface = {
    new FPlayer(
      lifePoints,
      armor + a.protection,
      strength,
      range,
      actualField,
      area,
      name,
      actionCounter,
      equipedWeapon = equipedWeapon)
  }

  override def equipWeapon(weapon: WeaponInterface): FPlayerInterface = {
    new FPlayer(
      lifePoints,
      armor,
      strength,
      range,
      actualField,
      area,
      name,
      actionCounter,
      equipedWeapon = weapon
    )
  }

  override def drop(item: Item): FPlayerInterface = ???

  override def walk(): FCharacterInterface = ???

}
