package de.htwg.se.zombiezite.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ItemDeckSpec extends WordSpec with Matchers {

  "An ItemDeck" can {
    var deck = baseImpl.ItemDeck()
    "have at least one Weapon" in {
      deck.weapons.isEmpty should be(false)
    }
    "have at least one Armor" in {
      deck.armors.isEmpty should be(false)
    }
    "have at least one Trash" in {
      deck.trash.isEmpty should be(false)
    }
    "have a complete Deck with at least one Item" in {
      deck.deck.isEmpty should be(false)
    }
    "have a first Item in Weapon" in {
      deck.weapons(0) should be(baseImpl.Weapon("Axe", 30, 1))
    }
    "have a first Item in Armor" in {
      deck.armors(0) should be(baseImpl.Armor("Holy Armor", 60))
    }
    "have a first Item in Trash" in {
      deck.trash(0) should be(baseImpl.Trash("Rice"))
    }
    "shuffle without harming the deck" in {
      val oldDeck = deck.deck
      deck.shuffle()
      deck.deck.length should be(oldDeck.length)
    }
    "draw and remove an Item" in {
      val oldDeckL = deck.deck.length
      deck.draw()
      deck.deck.length should be(oldDeckL - 1)
    }
  }
}