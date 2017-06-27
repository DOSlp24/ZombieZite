package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends WordSpec with Matchers {

  "A Player" can {
    var a = baseImpl.Area(10, 10)
    "exists init" should {
      val p = new baseImpl.Player(a, "Franz")
      "have Lifepoints" in {
        p.lifePoints should be(100)
      }
      "have a range" in {
        p.range should be(0)
      }
      "have an empty eq" in {
        p.equipment.isEmpty should be(true)
      }
      "do Damage" in {
        p.attack(3) should be(p.strength + 1)
      }
      "do critical Damage" in {
        p.attack(1) should be((p.strength + 1) * 2)
      }
      "have a name" in {
        p.name should be("Franz")
      }
    }
    "exists modified" should {
      val p = new baseImpl.Player(a, "")
      p.equippedWeapon = baseImpl.Weapon("Weapon", 20, 1)
      p.lifePoints = 2000
      "have some more Lifepoints" in {
        p.lifePoints should be(2000)
      }
      "do some more Damage" in {
        assert(p.attack(3) > p.strength)
      }
      "do Damage + Weapondmg" in {
        p.attack(3) should be(p.strength + p.equippedWeapon.strength)
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
      val p = baseImpl.Player(a, "")
      p.actualField = a.line(0)(0)
      p.actualField.players.append(p)
      p.actualField.chars.append(p)
      "have a start Field" in {
        p.actualField should be(baseImpl.Field(baseImpl.Position(0, 0)))
      }
      "not move left over border" in {
        p.walk(-1, 0) should be(false)
        p.actualField should be(baseImpl.Field(baseImpl.Position(0, 0)))
      }
      "not move down over border" in {
        p.walk(0, -1) should be(false)
        p.actualField should be(baseImpl.Field(baseImpl.Position(0, 0)))
      }
      "move right" in {
        p.walk(1, 0) should be(true)
        p.actualField should be(baseImpl.Field(baseImpl.Position(2, 0)))
      }

      "move left" in {
        p.walk(-1, 0) should be(true)
        p.actualField should be(baseImpl.Field(baseImpl.Position(0, 0)))
      }
      "move up" in {
        p.walk(0, 1) should be(true)
        p.actualField should be(baseImpl.Field(baseImpl.Position(0, 2)))
      }
      "move down" in {
        p.walk(0, -1) should be(true)
        p.actualField should be(baseImpl.Field(baseImpl.Position(0, 0)))
      }
      "not move up over border" in {
        p.actualField = a.line(9)(9)
        p.walk(0, 1) should be(false)
        p.actualField should be(baseImpl.Field(baseImpl.Position(18, 18)))
      }
      "not move right over border" in {
        p.walk(1, 0) should be(false)
        p.actualField should be(baseImpl.Field(baseImpl.Position(18, 18)))
      }
      "not leave Field twice" in {
        p.leaveField()
        p.leaveField()
      }
      "not leave Field twice again" in {
        p.actualField.players.append(p)
        p.leaveField()
      }
    }
    "die" in {
      val p = baseImpl.Player(a, "")
      p.actualField = a.line(3)(3)
      p.actualField.chars.append(p)
      p.actualField.players.append(p)
      p.die() should be("AAAAAAAAAAAAAHHHHHHHHHHHHHH!!! ICH STERBE!!!!! \n*Er ist jetzt tot*")
    }
    "have some eq" should {
      val p = baseImpl.Player(a, "")
      "can pick up new eq" in {
        p.equip(baseImpl.Weapon("Axe", 1, 1)) should be(true)
      }
      "remove an Item when dropped" in {
        p.equipment.clear()
        p.equipment.append(baseImpl.Armor("Armor", 10))
        p.equipment.append(baseImpl.Trash(" "))
        p.equipment.append(baseImpl.Armor("Armor2", 10))
        p.drop(baseImpl.Trash(" ")) should be(baseImpl.Trash(" "))

      }
      "cannot pick up too much" in {
        p.equipment.clear()
        p.equipment.appendAll(Array(baseImpl.Trash(" "), baseImpl.Trash(" "), baseImpl.Trash(" "), baseImpl.Trash(" "), baseImpl.Trash(" "), baseImpl.Trash(" "), baseImpl.Trash(" ")))
        p.equip(baseImpl.Trash(" ")) should be(false)
      }
      "can drop an Item" in {
        p.equipment.clear()
        p.equipment.append(baseImpl.Trash(" "))
        p.drop(baseImpl.Trash(" ")) should be(baseImpl.Trash(" "))
      }
      "cant drop an Item wich ist not in inv" in {
        p.drop(baseImpl.Trash("Not in my Inventory")) should be(null)
      }
      "use Armor" in {
        p.useArmor(baseImpl.Armor("Shield", 10)) should be(true)
      }
      "use Healkit" in {
        p.useArmor(baseImpl.Armor("Healkit", 10)) should be(true)
      }
    }
  }
}