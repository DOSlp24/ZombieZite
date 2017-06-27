package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FieldSpec extends WordSpec with Matchers {

  "A Field" can {
    val p = baseImpl.Position(0, 0)
    val f = baseImpl.Field(p)
    "have no chars" in {
      f.chars.isEmpty should be(true)
    }
    "have some chars" in {
      f.chars.append(baseImpl.Player(null, " "))
      f.chars.isEmpty should be(false)
    }
    "have no zombies" in {
      f.zombies.isEmpty should be(true)
    }
    "have some zombies" in {
      f.zombies.append(baseImpl.Zombie(null, " ", 0, 0, 0))
      f.zombies.isEmpty should be(false)
    }
    "have no players" in {
      f.players.isEmpty should be(true)
    }
    "have some players" in {
      f.players.append(baseImpl.Player(null, " "))
      f.players.isEmpty should be(false)
    }
    "have a charCount" in {
      f.charCount should be(0)
    }
    "change charCount" in {
      f.charCount = 1
      f.charCount should be(1)
    }
    "have a noiseCounter" in {
      f.noiseCounter should be(0)
    }
    "change noiseCounter" in {
      f.noiseCounter = 1
      f.noiseCounter should be(1)
    }
    "have a Position" in {
      f.p should be(baseImpl.Position(0, 0))
    }
  }
}