package de.htwg.se.zombiezite

import akka.actor._
import de.htwg.se.zombiezite.HotelZite.Receptionist.CheckIn

object HotelZite {
  val system = ActorSystem("HotelZite")
  val receptionist: ActorRef = system.actorOf(Props[Receptionist], "receptionist")

  val floors = 8
  val rooms = 15
  var roomArray: Array[Array[Room]] = Array.ofDim[Room](floors, rooms)
  var keys: Array[Key] = new Array[Key](floors * rooms)
  /*
  def main(args: Array[String]): Unit = {
    hotelMain
  }
  */
  def hotelMain: Unit = {
    for {
      floor <- 0 until floors
      room <- 0 until rooms
    } roomArray(floor)(room) = Room(floor, room)
    for {
      floor <- 0 until floors
      room <- 0 until rooms
    } keys(floor * rooms + room) = Key(floor, room)
    while (keys.nonEmpty) {
      val ran = scala.util.Random.nextInt(2)
      if (ran == 0) {
        val guest = system.actorOf(Props[HumanGuest], "human" + java.util.UUID.randomUUID())
      } else {
        val guest = system.actorOf(Props[ZombieGuest], "zombie" + java.util.UUID.randomUUID())
      }
    }
    printHotelZite()
    System.exit(1)
  }

  def printHotelZite(): Unit = {
    println("                  _________")
    println("               __/         \\__")
    println("            __/               \\__")
    println("          _/                     \\_")
    println("        _/                         \\_")
    println("      _/                             \\_")
    println("    _/                                 \\_")
    println("  _/                                     \\_")
    println(" /                                         \\")
    println("*********************************************")
    roomArray.foreach(floor => {
      floor.foreach(room => {
        print("#")
        if (room.guest.path.name.contains("human")) {
          print("H")
        } else {
          print("Z")
        }
        print("#")
      })
      println("\n*********************************************")
    })
    println("********************[R]**********************")
  }

  object Receptionist {

    case class CheckIn(guest: ActorRef)

    case class CheckOut(guest: ActorRef, room: Key)

  }

  class Receptionist extends Actor {

    import Receptionist._

    override def receive: Receive = {
      case CheckIn(sender) => {
        if (keys.nonEmpty) {
          sender ! keys.head
          roomArray(keys.head.floor)(keys.head.room) = Room(keys.head.floor, keys.head.room, sender, true)
          keys = keys.drop(1)
        } else {
          sender ! HotelFull
        }
      }
      case CheckOut(sender, room: Key) => {
        keys = keys :+ room
        roomArray(room.floor)(room.room) = Room(room.floor, room.room)
      }
    }
  }

  case class Key(floor: Integer, room: Integer)

  case class Room(floor: Integer, room: Integer, guest: ActorRef = null, taken: Boolean = false)

  case class HotelFull()

  abstract class Guest extends Actor

  class ZombieGuest extends Guest {
    var room: Key = null
    receptionist ! CheckIn(self)

    override def receive: Receive = {
      case key: Key => room = key
      case _: HotelFull => die()
    }

    def die(): Unit = {
      system.stop(self)
    }
  }

  class HumanGuest extends Guest {
    var room: Key = null
    receptionist ! CheckIn(self)

    override def receive: Receive = {
      case key: Key => room = key
      case _: HotelFull => die()
    }

    def die(): Unit = {
      system.stop(self)
    }
  }

}

