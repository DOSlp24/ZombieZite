package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{FDeckInterface, FItemInterface}

import scala.collection.mutable.ArrayBuffer

case class FItemDeck() extends FDeckInterface[FItemInterface] {
  //TODO: why does it not work ............. (Cannot resolve Symbol Array)?
  val weapons: Array[FItemInterface] = Array[FItemInterface](FWeapon("Axe", 30, 1), FWeapon("Pistol", 40, 3), FWeapon("Pistol", 40, 3),
    FWeapon("Pistol", 40, 3), FWeapon("Mashine Gun", 70, 3), FWeapon("Mashine Gun", 70, 3), FWeapon("Sniper", 40, 5),
    FWeapon("Sniper", 40, 5), FWeapon("Flame Thrower", 100, 2, 1), FWeapon("Knife", 10, 1), FWeapon("Knife", 10, 1),
    FWeapon("Knife", 10, 1), FWeapon("Shotgun", 100, 1), FWeapon("Shotgun", 100, 1),
    FWeapon("Big Mama", 150, 2), FWeapon("EVIL SISTERS", 200, 1), FWeapon("Pan", 5, 1))

  val armors: Array[FItemInterface] = Array[FItemInterface](FArmor("Holy Armor", 60), FArmor("Chest", 40), FArmor("Boots", 20), FArmor("Boots", 20),
    FArmor("Boots", 20), FArmor("Swat-Shield", 100), FArmor("Healkit", 10), FArmor("Healkit", 10), FArmor("Healkit", 10))

  val trash = Array[Trash](Trash("Rice"), Trash("Rice"), Trash("Rice"), Trash("Canned Food"), Trash("Canned Food"), Trash("Water"),
    Trash("Water"), Trash("Stone"), Trash("Trash"), Trash("Trash"), Trash("Trash"), Trash("Trash"), Trash("Trash"), Trash("Bottle"), Trash("Money"))

  val deck = ArrayBuffer[FItemInterface]()

  deck ++= weapons
  deck ++= armors
  deck ++= trash

  def shuffle(): Unit = {
    //needs to be var because of the shuffle process
    var newDeck = deck.toArray
    newDeck = shuffle(newDeck)
    deck.clear()
    deck ++= newDeck
  }

  def shuffle[Item](array: Array[Item]): Array[Item] = {
    val rnd = new java.util.Random
    for (n <- Iterator.range(array.length - 1, 0, -1)) {
      val k = rnd.nextInt(n + 1)
      val t = array(k); array(k) = array(n); array(n) = t
    }
    array
  }

  def draw(): FItemInterface = {
    shuffle()
    deck.remove(0)
  }

}