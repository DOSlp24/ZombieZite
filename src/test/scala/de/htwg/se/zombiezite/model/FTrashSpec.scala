package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FTrashSpec extends WordSpec with Matchers {

  "Trash" can {
    "have a name" in {
      val t = baseImpl.FTrash("trash")
      t.name should be("trash")
    }
  }
}