package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ArmorSpec extends WordSpec with Matchers {

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