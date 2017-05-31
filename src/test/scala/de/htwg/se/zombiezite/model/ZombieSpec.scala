package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ZombieSpec extends WordSpec with Matchers {

  "A Zombie" can {
    val a = Area(10, 10)
    val z = Zombie(a, "zombie", 1, 1, 100)
    z.actualField = a.line(3)(3)
    z.actualField.chars.append(z)
    z.actualField.zombies.append(z)
    "die" in {
      z.die() should be("Ein zombie weniger")
    }
    "stay on a Field" in {
      z.actualField should be(a.line(3)(3))
    }
    "exists init" should {
      "have Lifepoints" in {
        z.lp should be(100)
      }
      "change LP" in {
        z.lifePoints = 0
        z.lifePoints should be(0)
      }
      "have an area" in {
        z.area should be(a)
      }
      "have a range" in {
        z.range should be(1)
      }
      "do Damage" in {
        z.attack(3) should be(z.strength + 1)
      }
      "do critical Damage" in {
        z.attack(1) should be((z.strength + 1)*2)
      }
      "have strength" in {
        z.strength should be(1)
      }
      "have a name" in {
        z.name should be("zombie")
      }
      "have a target" in {
        z.targetField should be(null)
      }
      "change target Field" in {
        z.targetField = a.line(0)(0)
        z.targetField should be(Field(Position(0, 0)))
      }
      "have a distance to target" in {
        z.distanceToTargetField should be(1000)
      }
      "change distance" in {
        z.distanceToTargetField = 1
        z.distanceToTargetField should be(1)
      }
    }
    "walk" should {
      val z = Zombie(a, "zombie", 1, 1, 100)
      z.actualField = a.line(0)(0)
      z.actualField.zombies.append(z)
      z.actualField.chars.append(z)
      "have a start Field" in {
        z.actualField should be(Field(Position(0, 0)))
      }
      "not move left over border" in {
        z.walk(-1, 0) should be(false)
        z.actualField should be(Field(Position(0, 0)))
      }
      "not move down over border" in {
        z.walk(0, -1) should be(false)
        z.actualField should be(Field(Position(0, 0)))
      }
      "move right" in {
        z.walk(1, 0) should be(true)
        z.actualField should be(Field(Position(2, 0)))
      }

      "move left" in {
        z.walk(-1, 0) should be(true)
        z.actualField should be(Field(Position(0, 0)))
      }
      "move up" in {
        z.walk(0, 1) should be(true)
        z.actualField should be(Field(Position(0, 2)))
      }
      "move down" in {
        z.walk(0, -1) should be(true)
        z.actualField should be(Field(Position(0, 0)))
      }
      "not move up over border" in {
        z.actualField = a.line(9)(9)
        z.walk(0, 1) should be(false)
        z.actualField should be(Field(Position(18, 18)))
      }
      "not move right over border" in {
        z.walk(1, 0) should be(false)
        z.actualField should be(Field(Position(18, 18)))
      }
      "not leave Field twice" in{
        z.leaveField()
        z.leaveField()
      }
      "not leave Field twice again" in{
        z.actualField.zombies.append(z)
        z.leaveField()
      }
    }
  }
}