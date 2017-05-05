package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TrashSpec extends WordSpec with Matchers {
  
  "Trash" can {
    val t = Trash("trash")
    "have a name" in {
      t.name should be("trash")
    }
    "have no range" in {
      t.range should be(0)
    }
    "have no dmg" in {
      t.strength should be(0)
    }
  }
}