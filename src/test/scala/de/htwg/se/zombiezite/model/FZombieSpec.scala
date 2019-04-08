package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FZombieSpec extends WordSpec with Matchers {

  "A Zombie" can {
    val zLifePoints = 100
    val zombie = baseImpl.FZombie(zLifePoints, 3, 3, name = "zombie")
    "stay on a Field" in {
      zombie.x should be(3)
      zombie.y should be(3)
    }
    "exist and as default" should {
      "have life points" in {
        zombie.lifePoints should be(zLifePoints)
      }
      "have a range" in {
        zombie.range should be(0)
      }
      "have strength" in {
        zombie.strength should be(3)
      }
      "have a name" in {
        zombie.name should be("zombie")
      }
      "have a target" in {
        zombie.archenemy shouldBe a[FPlayerInterface]
      }
    }
    "walk and" should {
      val z1 = baseImpl.FZombie(zLifePoints, 0, 0, name = "zombie")
      // TODO: Whats the maximum Dimension of the Arena?
      val MAX_DIM = 10
      val z2 = baseImpl.FZombie(zLifePoints, MAX_DIM, MAX_DIM, name = "Big Boy")
      "start from a Field" in {
        z1.x should be(0)
        z1.y should be(0)
      }
      "move right" in {
        val new_z = baseImpl.FZombie(zLifePoints, 1, 0, name = "zombie")
        z1.walk(1, 0).x should be(new_z.x)
        z1.walk(1, 0).y should be(new_z.y)
      }
      "move left" in {
        val new_r = baseImpl.FZombie(zLifePoints, -1, 0, name = "zombie")
        z1.walk(-1, 0).x should be(new_r.x)
        z1.walk(-1, 0).y should be(new_r.y)
      }
      "move up" in {
        val new_r = baseImpl.FZombie(zLifePoints, 0, 1, name = "zombie")
        z1.walk(0, 1).x should be(new_r.x)
        z1.walk(0, 1).y should be(new_r.y)
      }
      "move down" in {
        val new_r = baseImpl.FZombie(zLifePoints, 0, -1, name = "zombie")
        z1.walk(0, -1).x should be(new_r.x)
        z1.walk(0, -1).y should be(new_r.y)
      }
      // TODO: is this even implemented?
      "not move right over border" ignore {
        z1.walk(1, 0) should be(z1)
      }
      "not move left over border" ignore {
        z1.walk(-1, 0) should be(z1)
      }
      "not move up over border" ignore {
        z1.walk(0, 1) should be(z1)
      }
      "not move down over border" ignore {
        z1.walk(0, -1) should be(z1)
      }
    }
    "handle taking damage on life points" in {
      zombie.takeDmg(2).lifePoints should be (zombie.lifePoints - 2)
    }
    "select a target" in {
      val player = baseImpl.FPlayer(0, 0, name = "Trainings target")
      val new_z = zombie.selectTarget(player)
      new_z.archenemy should be(player)
    }

  }
}