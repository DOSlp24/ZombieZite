package de.htwg.se.zombiezite.model

import de.htwg.se.zombiezite.model.baseImpl.{ FWeapon, FArmor, FTrash,FItemDeck }
import de.htwg.se.zombiezite.model.FItemInterface
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FItemDeckSpec extends WordSpec with Matchers {

  "An ItemDeck" can {
    val deck = baseImpl.FItemDeck()
    val filledDeck = deck.shuffle().asInstanceOf[FItemDeck]
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
      filledDeck.passedDeck.isEmpty should be(false)
    }
    "have a first Item in Weapon" in {
      deck.weapons(0) shouldBe a[FWeapon]
    }
    "have a first Item in Armor" in {
      deck.armors(0) shouldBe a[FArmor]
    }
    "have a first Item in Trash" in {
      deck.trash(0) shouldBe a[FTrash]
    }
    "shuffle without harming the deck" in {
      val oldDeckL = filledDeck.passedDeck.length
      filledDeck.shuffle()
      filledDeck.passedDeck.length should be(oldDeckL)
    }
    // TODO: Test ignored bc functionality not implemented yet
    // "remove an Item" in {
    "remove an Item" ignore {
      val oldDeckL = filledDeck.passedDeck.length
      filledDeck.draw()
      filledDeck.passedDeck.length should be(oldDeckL - 1)
    }
    // TODO: Test ignored bc functionality not implemented yet
    // "draw an Item" in {
    "draw an Item" ignore {
      filledDeck.draw() shouldBe a[FItemInterface]
    }
  }
}