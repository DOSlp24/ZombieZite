package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FAreaSpec extends WordSpec with Matchers {

  "An Area" can {
    val DIM = 10;
    val a = baseImpl.FArea(DIM, DIM).build()
    "exist and" should {
      "have a size" in {
        a.len should be(DIM)
      }
      "have a width" in {
        a.wid should be(DIM)
      }
      "have a Field" in {
        a.lines(1)(1).isInstanceOf[baseImpl.FField] should be(true)
      }
      "have a line length" in {
        a.lines.length should not be (0)
      }
      "have a non empty line" in {
        a.lines.isEmpty should be(false)
      }

    }
  }
}