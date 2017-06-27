package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WeaponSpec extends WordSpec with Matchers {

  "A Weapon" can {
    "exists" should {
      val w = baseImpl.Weapon("Weapon", 20, 2)
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
}