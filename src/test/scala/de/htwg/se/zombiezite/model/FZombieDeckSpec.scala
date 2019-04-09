package de.htwg.se.zombiezite.model

import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FZombieDeckSpec extends WordSpec with Matchers {

  "A Zombie Deck" can {
    //val a = baseImpl.Area(10, 10)
    val zDeck = baseImpl.FZombieDeck()
    "return a Zombie by draw" in {
      zDeck.getDrawnZombie(0) shouldBe a[FZombieInterface]
    }
    "return a special 1Zombie by draw" in {
      zDeck.getDrawnZombie(1).name should not be("Schlurfer")
    }
    "return a special 2Zombie by draw" in {
      zDeck.getDrawnZombie(2).name should not be("Schlurfer")
    }
    "return a special 3Zombie by draw" in {
      zDeck.getDrawnZombie(3).name should not be("Schlurfer")
    }
    "return a special 4Zombie by draw" in {
      zDeck.getDrawnZombie(4).name should not be("Schlurfer")
    }
    "return default zombie on invalid zombie number" in {
      zDeck.getDrawnZombie(-1) shouldBe a[FZombieInterface]
    }
    "return some zombies by draw" in {
      zDeck.draw() shouldBe a[Array[_]]
    }
  }
}