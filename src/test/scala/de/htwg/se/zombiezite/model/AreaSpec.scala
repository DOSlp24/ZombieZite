package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AreaSpec extends WordSpec with Matchers {

  "An Area" can {
    var a = baseImpl.Area(10, 10)
    "exists" should {
      "have a size" in {
        a.laenge should be(10)
      }
      "have a width" in {
        a.breite should be(10)
      }
      "have a Field" in {
        a.line(5)(3) should be(baseImpl.Field(baseImpl.Position(10, 6)))
      }
      "have a linelength" in {
        a.line.length should be(10)
      }
      "change a line" in {
        a.line(0)(0) = baseImpl.Field(baseImpl.Position(-1, -1))
        a.line(0)(0) should be(baseImpl.Field(baseImpl.Position(-1, -1)))
      }
      "have a non emty line" in {
        a.line.isEmpty should be(false)
      }
    }
  }
}