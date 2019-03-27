package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TrashSpec extends WordSpec with Matchers {

  "Trash" can {
    val t = baseImpl.Trash("trash")
    "have a name" in {
      t.name should be("trash")
    }
  }
}