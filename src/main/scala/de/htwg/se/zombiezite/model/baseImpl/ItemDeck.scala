package de.htwg.se.zombiezite.model.baseImpl

import scala.collection.mutable.ArrayBuffer
import de.htwg.se.zombiezite.model.{Item, Deck}

case class ItemDeck() extends Deck[Item] {
  var weapons = Array[Weapon](Weapon("Axe", 30, 1), Weapon("Pistol", 40, 3), Weapon("Mashine Gun", 70, 3),
    Weapon("Sniper", 40, 5), Weapon("Flame Thrower", 100, 2), Weapon("Knife", 10, 1), Weapon("Shotgun", 100, 1),
    Weapon("Big Mama", 150, 2), Weapon("EVIL SISTERS", 200, 1), Weapon("Pan", 5, 1))
  weapons.find { w => w.name == "Flame Thrower" }.get.aoe = 1
  var armors = Array[Armor](Armor("Holy Armor", 60), Armor("Chest", 40), Armor("Boots", 20), Armor("Swat-Shield", 100), Armor("Healkit", 10))

  var trash = Array[Trash](Trash("Rice"), Trash("Rice"), Trash("Rice"), Trash("Canned Food"), Trash("Canned Food"), Trash("Water"),
    Trash("Water"), Trash("Stone"), Trash("Trash"), Trash("Trash"), Trash("Trash"), Trash("Trash"), Trash("Trash"), Trash("Bottle"), Trash("Money"))

  var deck = ArrayBuffer[Item]()

  deck ++= weapons
  deck ++= armors
  deck ++= trash

  def shuffle(): Unit = {
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
    return array
  }

  def draw(): Item = {
    shuffle()
    var drawnCard = deck(0)
    deck.remove(0)
    return drawnCard
  }

}