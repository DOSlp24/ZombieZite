package de.htwg.se.zombiezite.MicroServices

import scala.util.Random
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

object MicroItemDeckMain {
  def main(args: Array[String]): Unit = {

    val db = Database.forConfig("zombieDb")
    try {
      class counterClass(tag: Tag) extends Table[(Int, Int, String)](tag, "Counter") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def counter = column[Int]("Counter")

        def name = column[String]("Name")

        def * = (id, counter, name)
      }
      val counterTable = TableQuery[counterClass]

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
