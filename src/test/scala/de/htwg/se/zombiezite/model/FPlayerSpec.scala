package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FPlayerSpec extends WordSpec with Matchers {

  "A Player" can {
    "exist with default stats" can {
      val p = baseImpl.FPlayer(0, 0, name = "Franz")
      "life points" in {
        val defaultLife = 100
        p.lifePoints should be(defaultLife)
      }
      "range" in {
        p.range should be(0)
      }
      "equipment" in {
        p.equipment.isEmpty should be(true)
      }
      "armor" in {
        p.armor should be(0)
      }
      "action counter" in {
        p.actionCounter should be(3)
      }
      "name" in {
        p.name should be("Franz")
      }
    }
    "exist with modified stats" can {
      val lp_mod = 2000
      val weapon_mod = baseImpl.FWeapon("Weapon", 2, 1)
      val p = baseImpl.FPlayer(0, 0, lifePoints = lp_mod, name = "", equippedWeapon = weapon_mod)

      "life points" in {
        p.lifePoints should be(lp_mod)
      }
      "a Weapon" in {
        p.equippedWeapon should be(weapon_mod)
      }
    }
    "walk and" should {
      val p1 = baseImpl.FPlayer(0, 0, name = "Franz")
      val MAX_DIM = 10
      val p2 = baseImpl.FPlayer(MAX_DIM, MAX_DIM, name = "Peta")
      "start from a Field" in {
        p1.x should be(0)
        p1.y should be(0)
      }
      "move right" in {
        val new_p = baseImpl.FPlayer(1, 0, name = "Franz")
        p1.walk(1, 0).x should be(new_p.x)
        p1.walk(1, 0).y should be(new_p.y)
      }
      "move left" in {
        val new_p = baseImpl.FPlayer(-1, 0, name = "Franz")
        p1.walk(-1, 0).x should be(new_p.x)
        p1.walk(-1, 0).y should be(new_p.y)
      }
      "move up" in {
        val new_p = baseImpl.FPlayer(0, 1, name = "Franz")
        p1.walk(0, 1).x should be(new_p.x)
        p1.walk(0, 1).y should be(new_p.y)
      }
      "move down" in {
        val new_p = baseImpl.FPlayer(0, -1, name = "Franz")
        p1.walk(0, -1).x should be(new_p.x)
        p1.walk(0, -1).y should be(new_p.y)
      }
      "not move right over border" ignore {
        p1.walk(1, 0) should be(p1)
      }
      "not move left over border" ignore {
        p1.walk(-1, 0) should be(p1)
      }
      "not move up over border" ignore {
        p1.walk(0, 1) should be(p1)
      }
      "not move down over border" ignore {
        p1.walk(0, -1) should be(p1)
      }
    }

    "handle equipment" can {
      // initialize Items
      val arnolds_weapon = baseImpl.FWeapon("AMT Longslide", 2, 1)
      val arnolds_armor = baseImpl.FArmor("Endo skeleton", 3)
      val trash1 = baseImpl.FTrash("T101")
      val trash2 = baseImpl.FTrash("T1000")

      val equip = Array[FItemInterface](trash1, trash2, arnolds_armor, arnolds_weapon)
      val p = baseImpl.FPlayer(0, 0, name = "Terminator", equipment = equip)
      "and remove an Item when dropped" in {
        p.drop(trash1) should not be (p)
      }
      "and use Armor" in {
        val new_p: FPlayerInterface = p.equip(arnolds_armor)
          .drop(arnolds_armor)
        new_p.armor should be(3)
        new_p.equipment should not contain arnolds_armor
      }
      "and use weapons" in {
        val new_p: FPlayerInterface = p.equip(arnolds_weapon)
          .drop(arnolds_weapon)
        new_p.equippedWeapon should be(arnolds_weapon)
        new_p.equipment should not contain arnolds_weapon
      }
    }
    "handle taking damage" can {
      val p = baseImpl.FPlayer(0, 0, name = "Crash test dummy")
      "on armor" in {
        val ARMOR_VALUE = 20
        val DMG_VALUE = 10
        val new_p = p.equip(baseImpl.FArmor("Seat belt", ARMOR_VALUE))
        new_p.takeDmg(DMG_VALUE).lifePoints should be(p.lifePoints)
        new_p.takeDmg(DMG_VALUE).armor should be(ARMOR_VALUE - DMG_VALUE)

      }
      "on life points" in {
        p.takeDmg(2).lifePoints should be(p.lifePoints - 2)
      }
    }
  }
}