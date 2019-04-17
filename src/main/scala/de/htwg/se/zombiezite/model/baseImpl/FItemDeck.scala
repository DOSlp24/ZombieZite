package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.CustomTypes.ItemMonad
import de.htwg.se.zombiezite.model.{ FArmorInterface, FDeckInterface, FItemInterface, FWeaponInterface }

case class FItemDeck(
    weapons: Vector[FWeaponInterface] = Vector[FWeaponInterface](FWeapon("Axe", 30, 1), FWeapon("Pistol", 40, 3), FWeapon("Pistol", 40, 3),
      FWeapon("Pistol", 40, 3), FWeapon("Mashine Gun", 70, 3), FWeapon("Mashine Gun", 70, 3), FWeapon("Sniper", 40, 5),
      FWeapon("Sniper", 40, 5), FWeapon("Flame Thrower", 100, 2, 1), FWeapon("Knife", 10, 1), FWeapon("Knife", 10, 1),
      FWeapon("Knife", 10, 1), FWeapon("Shotgun", 100, 1), FWeapon("Shotgun", 100, 1),
      FWeapon("Big Mama", 150, 2), FWeapon("EVIL SISTERS", 200, 1), FWeapon("Pan", 5, 1)),
    armors: Vector[FArmorInterface] = Vector[FArmorInterface](FArmor("Holy Armor", 60), FArmor("Chest", 40), FArmor("Boots", 20), FArmor("Boots", 20),
      FArmor("Boots", 20), FArmor("Swat-Shield", 100), FArmor("Healkit", 10), FArmor("Healkit", 10), FArmor("Healkit", 10)),
    trash: Vector[FItemInterface] = Vector[FItemInterface](FTrash("Rice"), FTrash("Rice"), FTrash("Rice"), FTrash("Canned Food"), FTrash("Canned Food"), FTrash("Water"),
      FTrash("Water"), FTrash("Stone"), FTrash("Trash"), FTrash("Trash"), FTrash("Trash"), FTrash("Trash"), FTrash("Trash"), FTrash("Bottle"), FTrash("Money")),
    passedDeck: Vector[FItemInterface] = Vector[FItemInterface]()
) extends FDeckInterface {

  def shuffle(): FDeckInterface = {
    val combinedDeck: Vector[FItemInterface] = weapons ++ armors ++ trash
    FItemDeck(passedDeck = buildDeck(combinedDeck))
  }

  def buildDeck(combinedDeck: Vector[FItemInterface]): Vector[FItemInterface] = {
    if (combinedDeck.isEmpty) {
      Vector[FItemInterface]()
    } else {
      val rnd = new java.util.Random
      val index = rnd.nextInt(combinedDeck.length)
      buildDeck(combinedDeck patch (from = index, patch = Nil, replaced = 1)) :+ combinedDeck.apply(index)
    }
  }

  def draw(): ItemMonad[Option[FItemInterface]] = {
    if (passedDeck.nonEmpty) {
      new ItemMonad(Vector(Some(passedDeck.head)))
    } else {
      new ItemMonad(Vector(None))
    }
  }

  def afterDraw(): FDeckInterface = {
    copy(passedDeck = passedDeck.drop(1))
  }

}