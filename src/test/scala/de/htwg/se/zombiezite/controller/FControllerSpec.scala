package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model.baseImpl.FPlayer
import de.htwg.se.zombiezite.model.{ FItemInterface, FPlayerInterface, FZombieInterface }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class FControllerSpec extends WordSpec with Matchers {
  "A FController" can {
    val player: Vector[FPlayerInterface] = Vector(FPlayer(0, 0, 0, 0, 0, 0, "Franz", 3, Array[FItemInterface]()))
    val zombies: Vector[FZombieInterface] = Vector[FZombieInterface]()
    val c = new FController()
    val s = c.cState(0, player, zombies, 0, FPlayer(0, 0, 0, 0, 0, 0, "", 0, Array[FItemInterface]()))
    "increase Round counter" in {
      s.increaseRoundCount().round should be(1)
    }
    "increase Round double" in {
      s.increaseRoundCount().increaseRoundCount().round should be(2)
    }
  }

}
