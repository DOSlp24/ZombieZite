package de.htwg.se.zombiezite.util

import de.htwg.se.zombiezite.controller.{ FController, cState }
import org.junit.runner.RunWith
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.junit.JUnitRunner

import scala.concurrent.Await
import scala.concurrent.duration.Duration

@RunWith(classOf[JUnitRunner])
class SlickDAOSpec extends WordSpec with Matchers {
  "A SlickDAO" can {
    val c = new FController()
    val s = cState()
    val dao = SlickDAO()
    "instatiate" in {
      dao.create(s)
    }
    "read" in {
      dao.read()
    }
    "update" in {
      dao.update("area", s)
    }
    "delete" in {
      dao.delete("area")
    }
    "return a cState" in {
      dao.read().isInstanceOf[cState] should be
      true
    }
  }
}
