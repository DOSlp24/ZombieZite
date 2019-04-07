package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model.baseImpl.{ FPlayer, FZombie }
import de.htwg.se.zombiezite.model.{ FItemInterface, FPlayerInterface, FZombieInterface }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class FControllerSpec extends WordSpec with Matchers {
  "A FController" can {
    val player: Vector[FPlayerInterface] = Vector(FPlayer(0, 0))
    val zombies: Vector[FZombieInterface] = Vector[FZombieInterface]()
    val c = new FController()
    val s = c.cState()
    "increase Round counter" in {
      s.increaseRoundCount().round should be(1)
    }
    "increase Round double" in {
      s.increaseRoundCount().increaseRoundCount().round should be(2)
    }
    "push Player" in {
      s.enterField(player.apply(0)).area.lines(0)(0).players should be(player)
    }
    "push Zombie" in {
      s.enterField(FZombie(100, 5, 5)).zombies.length should be(1)
    }
    "Move Characters" can {
      val player: Vector[FPlayerInterface] = Vector(FPlayer(5, 5))
      val zombie: Vector[FZombieInterface] = Vector(FZombie(lifePoints = 100, x = 5, y = 5))
      val moveState = s.enterField(player.apply(0))
      "Player Move Down" in {
        moveState.moveDown(player.apply(0)).player.apply(0).y should be(6)
      }
      "Player Move Up" in {
        moveState.moveUp(player.apply(0)).player.apply(0).y should be(4)
      }
      "Player Move Right" in {
        moveState.moveRight(player.apply(0)).player.apply(0).x should be(6)
      }
      "Player Move Left" in {
        moveState.moveLeft(player.apply(0)).player.apply(0).x should be(4)
      }
      "Not Move Other Players" in {
        moveState.enterField(player.apply(0).walk(1, 0)).moveDown(player.apply(0)).player.apply(1).y should be(5)
      }

      "Zombie Move Down" in {
        moveState.moveDown(zombie.apply(0)).zombies.apply(0).y should be(6)
      }
      "Zombie Move Up" in {
        moveState.moveUp(zombie.apply(0)).zombies.apply(0).y should be(4)
      }
      "Zombie Move Right" in {
        moveState.moveRight(zombie.apply(0)).zombies.apply(0).x should be(6)
      }
      "Zombie Move Left" in {
        moveState.moveLeft(zombie.apply(0)).zombies.apply(0).x should be(4)
      }
      "Not Move Other Zombies" in {
        moveState.enterField(zombie.apply(0).walk(1, 0)).moveDown(zombie.apply(0)).zombies.apply(1).y should be(5)
      }
    }
    "Iterate over Players" can {
      val p1 = FPlayer(3, 3)
      val p2 = FPlayer(3, 3)
      val p3 = FPlayer(3, 3)
      val itState = c.cState().buildArea().enterField(p1).enterField(p2).enterField(p3)
      "Have a first" in {
        itState.actualPlayer should be(p1)
      }
      "Have a second" in {
        itState.nextPlayer().actualPlayer should be(p2)
      }
      "Have a third" in {
        itState.nextPlayer().nextPlayer().actualPlayer should be(p3)
      }
      "Iterate after last" in {
        itState.nextPlayer().nextPlayer().nextPlayer().actualPlayer should be(p1)
      }
    }

  }
}
