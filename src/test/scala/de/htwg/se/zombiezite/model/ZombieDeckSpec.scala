package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ZombieDeckSpec extends WordSpec with Matchers {
  
  "A Zombie Deck" can {
    val a = Area(10, 10)
    val zd = ZombieDeck(a)
    "return a Zombie by draw" in {
      zd.getDrawnZombie().getClass.toString() should be("class de.htwg.se.zombiezite.model.Zombie")
    }
    "return a Schlürfi" in {
      zd.getDrawnZombieR(0) should be(Zombie(a, "Schlurfer", 19, 1, 100))
    }
    "return a Runner" in {
      zd.getDrawnZombieR(1) should be(Zombie(a, "Runner", 9, 1, 70))
    }
    "return a Fatti" in {
      zd.getDrawnZombieR(2) should be(Zombie(a, "Fatti", 39, 1, 250))
    }
    "return a Tank" in {
      zd.getDrawnZombieR(3) should be(Zombie(a, "Tank", 49, 2, 500))
    }
    "return a Spitter" in {
      zd.getDrawnZombieR(4) should be(Zombie(a, "Spitter", 9, 3, 70))
    }
    "return nothing" in {
      zd.getDrawnZombieR(-1) should be(null)
    }
    "return some zombies by draw" in {
      zd.draw().getClass should be(Array[Zombie]().getClass)
    }
  }
}