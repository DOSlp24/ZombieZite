package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model.baseImpl.{ FPlayer, FZombie }
import de.htwg.se.zombiezite.model.{ FItemInterface, FPlayerInterface, FZombieInterface, baseImpl }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class FControllerSpec extends WordSpec with Matchers {
  "A FController" can {
    val player: Vector[FPlayerInterface] = Vector(FPlayer(0, 0))
    val zombies: Vector[FZombieInterface] = Vector[FZombieInterface]()
    val c = new FController()
    val s = cState()
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
      val itState = cState().buildArea().enterField(p1).enterField(p2).enterField(p3)
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
    "ZombieTurn" can {
      val z1 = FZombie(100, 5, 5)
      val z2 = FZombie(100, 6, 5)
      val z3 = FZombie(100, 9, 9)
      val ztState = cState().buildArea().enterField(z1).enterField(z2).enterField(z3)
      "Walk default" in {
        ztState.zombieTurn().zombies should not be ztState.zombies
      }
      "Walk towards Player X" in {
        val p1 = FPlayer(5, 9)
        ztState.enterField(p1).zombieTurn().zombies should contain(FZombie(100, 5, 6))
      }
      "Walk towards Player X Negative" in {
        val p1 = FPlayer(5, 0)
        ztState.enterField(p1).zombieTurn().zombies should contain(FZombie(100, 5, 4))
      }
      "Walk towards Player Y" in {
        val p1 = FPlayer(9, 5)
        ztState.enterField(p1).zombieTurn().zombies should contain(FZombie(100, 7, 5))
      }
      "Walk towards Player Y Negative" in {
        val p1 = FPlayer(0, 5)
        ztState.enterField(p1).zombieTurn().zombies should contain(FZombie(100, 4, 5))
      }

      "Walk away from borders" can {
        val z1 = FZombie(100, 9, 5)
        val z2 = FZombie(100, 0, 5)
        val z3 = FZombie(100, 5, 9)
        val z4 = FZombie(100, 5, 0)
        "Walk Left" in {
          cState().buildArea().enterField(z1).zombieTurn().zombies(0).x should be(8)
        }
        "Walk Right" in {
          cState().buildArea().enterField(z2).zombieTurn().zombies(0).x should be(1)
        }
        "Walk Up" in {
          cState().buildArea().enterField(z3).zombieTurn().zombies(0).y should be(8)
        }
        "Walk Down" in {
          cState().buildArea().enterField(z4).zombieTurn().zombies(0).y should be(1)
        }
      }
    }
  }
}
