package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FFieldSpec extends WordSpec with Matchers {

  "A Field" can {
    val pos = baseImpl.Position(0, 0)
    val emptyField = baseImpl.FField(pos)
    "have no chars" in {
      emptyField.chars.isEmpty should be(true)
    }
    "have no zombies" in {
      emptyField.zombies.isEmpty should be(true)
    }
    "have no players" in {
      emptyField.players.isEmpty should be(true)
    }

    val player = baseImpl.FPlayer(0,0)
    val zombie = baseImpl.FZombie(1,0,0)

    val n = emptyField.enterPlayer(player)
    val newField = n.enterZombie(zombie)
    "have some chars" in {
      newField.chars.isEmpty should be(false)
    }
    "have some zombies" in {
      newField.zombies.isEmpty should be(false)
    }
    "have some players" in {
      newField.players.isEmpty should be(false)
    }
    "have a charCount" in {
      emptyField.charCount should be(0)
    }
    "change charCount" in {
      newField.charCount should be(2)
    }
    "have a noiseCounter" in {
      emptyField.noiseCounter should be(0)
    }
    "have a Position" in {
      emptyField.p should be(baseImpl.Position(0, 0))
    }
  }
}