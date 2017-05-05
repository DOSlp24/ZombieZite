package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import de.htwg.se.zombiezite.model.Player;

@RunWith(classOf[JUnitRunner])
class ZombieZiteTester extends WordSpec with Matchers {

  "An Area" can {
    var a = Area(10, 10)
    "exists" should {
      "have a size" in {
        a.laenge should be(10)
      }
      "have a width" in {
        a.breite should be(10)
      }
      "have a Field" in {
        a.line(5)(3) should be(Field(Position(10, 6)))
      }
    }
  }

  "A Player" can {
    var a = Area(10, 10)
    "exists init" should {
      val p = new Player(a, "Franz")
      "have Lifepoints" in {
        p.lifePoints should be(100)
      }
      "do Damage" in {
        p.attack() should be(p.strength)
      }
      "have a name" in {
        p.name should be("Franz")
      }
    }
    "exists modified" should {
      val p = new Player(a, "")
      p.equippedWeapon = Weapon("Weapon", 20, 1)
      p.lifePoints = 2000
      "have some more Lifepoints" in {
        p.lifePoints should be(2000)
      }
      "do some more Damage" in {
        assert(p.attack() > p.strength)
      }
      "do Damage + Weapondmg" in {
        p.attack() should be(p.strength + p.equippedWeapon.strength)
      }
      "have a Weapon with a name" in {
        p.equippedWeapon.name should be("Weapon")
      }
      "have a Weapon with some Damage" in {
        p.equippedWeapon.strength should be(20)
      }
      "have a Weapon with some range" in {
        p.equippedWeapon.range should be(1)
      }
    }
    "walk" should {
      val p = Player(a, "")
      p.actualField = a.line(0)(0)
      p.actualField.players.append(p)
      p.actualField.chars.append(p)
      "have a start Field" in {
        p.actualField should be(Field(Position(0, 0)))
      }
      "not move left over border" in {
        p.walk(-1, 0) should be(false)
        p.actualField should be(Field(Position(0, 0)))
      }
      "not move down over border" in {
        p.walk(0, -1) should be(false)
        p.actualField should be(Field(Position(0, 0)))
      }
      "move right" in {
        p.walk(1, 0) should be(true)
        p.actualField should be(Field(Position(2, 0)))
      }

      "move left" in {
        p.walk(-1, 0) should be(true)
        p.actualField should be(Field(Position(0, 0)))
      }
      "move up" in {
        p.walk(0, 1) should be(true)
        p.actualField should be(Field(Position(0, 2)))
      }
      "move down" in {
        p.walk(0, -1) should be(true)
        p.actualField should be(Field(Position(0, 0)))
      }
      "not move up over border" in {
        p.actualField = a.line(9)(9)
        p.walk(0, 1) should be(false)
        p.actualField should be(Field(Position(18, 18)))
      }
      "not move right over border" in {
        p.walk(1, 0) should be(false)
        p.actualField should be(Field(Position(18, 18)))
      }
    }
  }

  "A Weapon" can {
    "exists" should {
      val w = Weapon("Weapon", 20, 2)
      "have a name" in {
        w.name should be("Weapon")
      }
      "have some Damage" in {
        w.strength should be(20)
      }
      "have a range" in {
        w.range should be(2)
      }
    }
  }

  "An Armor" can {
    "exists" should {
      val a = Armor("Armor", 20)
      "have a name" in {
        a.name should be("Armor")
      }
      "have some protection" in {
        a.protection should be(20)
      }
    }
  }
}
