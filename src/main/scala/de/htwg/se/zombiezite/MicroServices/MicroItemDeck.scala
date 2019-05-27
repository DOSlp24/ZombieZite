package de.htwg.se.zombiezite.MicroServices

import scala.io.StdIn
import scala.util.Random
// Use H2Driver to connect to an H2 database
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global

object MicroItemDeckMain {
  def main(args: Array[String]): Unit = {

    //TODO: This needs to be in a file named application.conf?
    h2mem1 = {
      url = "jdbc:h2:mem:test1"
      driver = org.h2.Driver
      connectionPool = disabled
      keepAliveConnection = true
    }

    val db = Database.forConfig("h2mem1")
    try {
      // TODO: schema ... Database stuff comes here
    } finally db.close

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
