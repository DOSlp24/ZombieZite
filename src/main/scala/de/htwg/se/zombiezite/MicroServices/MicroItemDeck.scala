package de.htwg.se.zombiezite.MicroServices

import scala.io.StdIn
import scala.util.Random

object MicroItemDeckMain {
  def main(args: Array[String]): Unit = {
    val webserver = new MicroItemDeckServer(new MicroItemDeck)
    println(s"Server online at http://localhost:8082/")
    while (true) {
      Thread.sleep(10000000)
    }
    webserver.unbind()
  }
}

class MicroItemDeck {
  var deck: List[String] = List("Machine Gun", "Rocket Launcher", "Knife", "Trash", "Water", "Armor", "Chest Plate", "Boots", "Pistol")

  def shuffle(): Unit = {
    deck = Random.shuffle(deck)
  }

  def draw: String = {
    if (deck.isEmpty) {
      "Deck Empty"
    } else {
      val item = deck.head
      deck = deck.drop(1)
      "You got " + item
    }
  }
}
