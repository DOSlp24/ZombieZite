package de.htwg.se.zombiezite.controller

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import de.htwg.se.zombiezite.model._

@RunWith(classOf[JUnitRunner])
class ControllerSpec extends WordSpec with Matchers {

  "A Controller" can {
    "exist" should {
      val c = new Controller()
      "have no area" in {
        c.area should be(null)
      }
      "have no players" in {
        c.player should be(null)
      }
      "have no zombies" in {
        c.zombies.isEmpty should be(true)
      }
      "have an ItemDeck" in {
        c.itemDeck should not be (null)
      }
      "have no zombieDeck" in {
        c.zombieDeck should be(null)
      }
      "have some Player names" in {
        c.playerNamer.isEmpty should be(false)
      }
    }
    "init" should {
      val c = new Controller()
      val c2 = new Controller()
      c.init(1)
      c.player(0).actualField = c.area.line(5)(5)
      "have a playerCount" in {
        c.player.length should be(1)
      }
      "have an area" in {
        c.area should be(Area(10, 10))
      }
      "have a zombieDeck" in {
        c.zombieDeck should not be (null)
      }
      "draw an Item" in {
        c.drawItem() should not be (null)
      }
      "draw a Zombie" in {
        c.drawZombie().getClass should be(Array[Zombie]().getClass)
      }
      "print playerPos" in {
        c.playerPos() should not be (null)
      }
      "print zombiePos" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies(c.zombies.length - 1).actualField = Field(Position(10, 0))
        c.zombiePos() should not be (null)
      }
      "print zombiePos without zombies" in {
        c2.zombies.clear()
        c2.zombiePos() should be("*Keine Zombies auf dem Spielfeld!*\n\n")
      }
      "have a Zombie walk left" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(14, 10))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "have a Zombie walk right" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(8, 10))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "have a Zombie walk up" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(10, 0))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "have a Zombie walk down" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(10, 14))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "have a Zombie attack a Player" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(10, 10))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "have a Zombie walk left wihout seeing" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(8, 8))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "have a Zombie walk up without seeing" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(0, 0))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "have a Zombie walk right without seeing" in {
        c.zombies.append(Zombie(c.area, "", 0, 0, 0))
        c.zombies.last.actualField = Field(Position(0, 18))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.zombieTurn(c.zombies.last) should be(true)
      }
      "move a Character" in {
        c.zombies.append(Zombie(c.area, "", 1, 1, 1))
        c.zombies.last.actualField = Field(Position(0, 0))
        c.zombies.last.actualField.zombies.append(c.zombies.last)
        c.zombies.last.actualField.chars.append(c.zombies.last)
        c.move(c.zombies.last, 1, 0) should be(true)
      }
      "search as Player with full inv" in {
        c.player(0).equipment.appendAll(Array(Trash(" "), Trash(" "), Trash(" "), Trash(" "), Trash(" "), Trash(" ")))
        c.search(c.player(0)) should be(false)
      }
      "search as Player" in {
        c.player(0).equipment.clear()
        c.search(c.player(0)) should be(true)
      }
      "drop as Player" in {
        c.drop(c.player(0), c.player(0).equipment(0))
        c.player(0).equipment.isEmpty should be(true)
      }
      "print Equipment" in {
        c.equip(c.player(0))
        c.player(0).equipment.isEmpty should be(true)
      }
      "beweapon a Player" should {
        val p = Player(null, "Name")
        val w = Weapon("Axe", 1, 1)
        p.equipment.clear()
        "beweapon with nothing" in {
          c.beweapon(p, null)
        }
        "beweapon with an Axe" in {
          c.beweapon(p, w)
          p.equippedWeapon should be(w)
        }
        "beweapon with another Axe while holding one" in {
          c.beweapon(p, w)
          p.equippedWeapon should be(w)
        }
        "beweapon with nothing while holding something" in {
          c.beweapon(p, null)
          p.equippedWeapon.name should be("Fist")
        }
        "beweapon with nothing holding something with full inv" in {
          p.equipment.appendAll(Array(Trash(" "), Trash(" "), Trash(" "), Trash(" "), Trash(" "), Trash(" ")))
          c.beweapon(p, w)
          c.beweapon(p, null)
        }
      }
      "attack Zombie" in {
        c.attackZombie(c.player(0), c.zombies.last) should be(1)
      }
      "attack low Zombie" in {
        c.zombies.last.lifePoints = 1
        c.player(0).equippedWeapon = Weapon("Axe", 10, 10)
        c.attackZombie(c.player(0), c.zombies.last)
      }

      "attack low Player" in {
        c.player(0).lifePoints = 1
        c.player(0).actualField.players.append(c.player(0))
        c.player(0).actualField.chars.append(c.player(0))
        c.zombies.append(Zombie(c.area, "Arg", 10, 10, 100))
        c.attackPlayer(c.player(0), c.zombies.last)
      }
    }
    "attackField" should {
      val c = new Controller()
      c.init(2)
      c.player(0).actualField = c.area.line(5)(5)
      c.player(1).actualField = c.area.line(5)(6)
      c.player(1).actualField.players.append(c.player(1))
      c.player(0).actualField.players.append(c.player(0))
      c.player(1).actualField.chars.append(c.player(1))
      c.player(0).actualField.chars.append(c.player(0))
      val pf = c.player(0).actualField
      "attack on an empty Field" in {
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 1)) should be(false)
      }
      "attack on an non empty Field Player" in {
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 1)) should be(true)
      }
      "attack on an non empty Field killing Player" in {
        c.player(1).lifePoints = 1
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 1)) should be(true)
      }
      val z1 = Zombie(c.area, "Spitter", 1, 1, 100)
      val z2 = Zombie(c.area, "Schlurfer", 1, 1, 100)
      val z3 = Zombie(c.area, "Fatti", 1, 1, 100)
      val z4 = Zombie(c.area, "Tank", 1, 1, 100)
      val z5 = Zombie(c.area, "MEGASUPERDUPERBOSS", 1, 1, 100)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 4).zombies.append(z1)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 4).chars.append(z1)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 3).zombies.append(z2)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 3).chars.append(z2)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 2).zombies.append(z3)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 2).chars.append(z3)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 3).zombies.append(z4)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 3).chars.append(z4)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 2).zombies.append(z5)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 2).chars.append(z5)
      "attack a Spitter" in {
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 4)) should be(true)
      }
      "attack a Schlurfer" in {
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 3)) should be(true)
      }
      "attack a Fatti" in {
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 2)) should be(true) 
      }
      "attack a Tank" in {
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 3)) should be(true)
      }
      "attack an other Type" in {
        c.attackField(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 2)) should be(true)
      }
    }
    "attack a whole Field" should {
      val c = new Controller()
      c.init(2)
      c.player(0).actualField = c.area.line(5)(5)
      c.player(1).actualField = c.area.line(5)(6)
      c.player(1).actualField.players.append(c.player(1))
      c.player(0).actualField.players.append(c.player(0))
      c.player(1).actualField.chars.append(c.player(1))
      c.player(0).actualField.chars.append(c.player(0))
      val pf = c.player(0).actualField
      val z1 = Zombie(c.area, "Spitter", 1, 1, 100)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 1).zombies.append(z1)
      c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 1).chars.append(z1)
      
      "attack whole Field empty" in {
        c.attackWholeFieldP(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength - 1)) should be(true)
      }
      "attack whole Field" in {
        c.attackWholeFieldP(c.player(0), c.area.line(pf.p.x / c.fieldlength)(pf.p.y / c.fieldlength + 1)) should be(true)
      }
      "kill yourself" in {
        c.player(0).walk(0, 1)
        c.player(1).lifePoints = 1
        c.player(0).lifePoints = 1
        c.attackWholeFieldP(c.player(0), c.player(0).actualField) should be(true)
      }
    }
  }
}