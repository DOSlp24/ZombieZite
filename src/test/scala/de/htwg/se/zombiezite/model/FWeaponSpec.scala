package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FWeaponSpec extends WordSpec with Matchers {

  "A Weapon" can {
    "exist and" should {
      val strength = 20
      val w = baseImpl.FWeapon("Weapon", strength, 2)
      "have a name" in {
        w.name should be("Weapon")
      }
      "have some Damage" in {
        w.strength should be(strength)
      }
      "have a range" in {
        w.range should be(2)
      }
      "have a aoe indicator" in {
        w.aoe should be(0)
      }
    }
  }
}