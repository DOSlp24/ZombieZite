package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FArmorSpec extends WordSpec with Matchers {

  "An Armor" can {
    "exist and" should {
      val STRENGTH = 20
      val a = baseImpl.FArmor("Armor", STRENGTH)
      "have a name" in {
        a.name should be("Armor")
      }
      "have some protection" in {
        a.protection should be(STRENGTH)
      }
    }
  }

}