package de.htwg.se.zombiezite.controller
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.util.Observable
import scala.collection.mutable.ArrayBuffer

class Controller() extends Observable {

  var area: Area = null
  var player: Array[Player] = null
  var zombies: ArrayBuffer[Zombie] = ArrayBuffer[Zombie]()
  var itemDeck = new ItemDeck()
  var zombieDeck: Deck[Array[Zombie]] = null
  val playerNamer: Array[String] = Array("F. Maiar", "K. Kawaguchi", "H. Kaiba", "P. B. Rainbow")
  var fieldlength = 0
  var zombieCount = 0
  var playerCount = 0
  var zombiesKilled = 0
  var dmg = 0
  var lastAttackedPlayer: Player = Player(area, "Fritz")
  var lastAttackedZombie: Zombie = Zombie(area, "", 0, 0, 0)
  var winCount = 50

  val GAME_OVER_LOST = -2
  val GAME_OVER_WON = -1

  val SUCCES = 0
  val FAIL = 1

  val ZOMBIE_WENT_UP = 2
  val ZOMBIE_WENT_DOWN = 3
  val ZOMBIE_WENT_RIGHT = 4
  val ZOMBIE_WENT_LEFT = 5
  val ZOMBIE_ATTACK = 6

  val DEAD = 7

  val DISCARD_WEAPON = 8
  val CANT_DISCARD_FISTS = 9
  val CANT_DISCARD_FULL_INV = 10
  val SWAPED_WEAPON = 11
  val EQUIPED_WEAPON = 12

  val ARMOR_DAMAGED = 13
  val ARMOR_DESTROYED = 14

  val PLAYER_ATTACK = 15
  val PLAYER_ATTACK_PLAYER = 16

  def init(playerCounter: Int) {
    this.playerCount = playerCounter
    player = new Array[Player](playerCounter)
    area = new Area(10, 10)
    fieldlength = area.line(0)(0).length
    zombieDeck = new ZombieDeck(area)
    val startField = area.line(0)(0)
    for (i <- 0 to player.length - 1) {
      player(i) = Player(area, playerNamer(i))
      player(i).actualField = startField
      player(i).actualField.players.append(player(i))
      player(i).actualField.chars.append(player(i))
    }
  }

  def drawItem(): Item = {
    if (itemDeck.deck.isEmpty) {
      return Trash("Trash")
    }
    return itemDeck.draw()
  }

  def attackableFields(actualField: Field, range: Int): Array[Field] = {
    var attackableFields = ArrayBuffer[Field]()
    for (r <- 0 to range) {
      if (actualField.p.x / fieldlength + r < area.laenge) {
        attackableFields.append(area.line(actualField.p.x / fieldlength + r)(actualField.p.y / fieldlength))
      }
      if (actualField.p.x / fieldlength - r >= 0) {
        attackableFields.append(area.line(actualField.p.x / fieldlength - r)(actualField.p.y / fieldlength))
      }
    }
    for (r <- 0 to range) {
      if (actualField.p.y / fieldlength + r < area.laenge) {
        attackableFields.append(area.line(actualField.p.x / fieldlength)(actualField.p.y / fieldlength + r))
      }
      if (actualField.p.y / fieldlength - r >= 0) {
        attackableFields.append(area.line(actualField.p.x / fieldlength)(actualField.p.y / fieldlength - r))
      }
    }
    return attackableFields.distinct.toArray
  }

  def availableWeapon(p: Player): Array[Int] = {
    var waffen = ArrayBuffer[Int]()
    for (ws <- 0 to p.equipment.length - 1) {
      if (p.equipment(ws).isInstanceOf[Weapon]) {
        waffen.append(ws)
      }
    }

    return waffen.toArray
  }

  def drawZombie(): Array[Zombie] = {
    var tempZombie = zombieDeck.draw()
    if (!tempZombie.isEmpty) {
      for (i <- 0 to tempZombie.length - 1) {
        zombies.append(tempZombie(i))
        zombieCount += 1
      }
    }
    return tempZombie
  }

  def zombieTurn(z: Zombie): Int = {
    for (j <- 0 to player.length - 1) {

      if (z.actualField.p.x == player(j).actualField.p.x) {

        if (z.actualField.p.y == player(j).actualField.p.y) {
          return attackPlayer(player(j), z)
        } else if (z.actualField.p.y < player(j).actualField.p.y) {
          z.walk(0, 1)
          return ZOMBIE_WENT_UP
        } else if (z.actualField.p.y > player(j).actualField.p.y) {
          z.walk(0, -1)
          return ZOMBIE_WENT_DOWN
        }
      } else if (z.actualField.p.y == player(j).actualField.p.y) {
        if (z.actualField.p.x < player(j).actualField.p.x) {
          z.walk(1, 0)
          return ZOMBIE_WENT_RIGHT
        } else if (z.actualField.p.x > player(j).actualField.p.x) {
          z.walk(-1, 0)
          return ZOMBIE_WENT_LEFT
        }
      }
    }
    if (z.walk(-1, 0)) {
      return ZOMBIE_WENT_LEFT
    } else if (z.walk(0, 1)) {
      return ZOMBIE_WENT_UP
    } else{
      return ZOMBIE_WENT_RIGHT
    }
  }

  def move(char: Character, x: Int, y: Int): Boolean = {
    return char.walk(x, y)
  }

  def search(p: Player): Item = {
    if (p.equipment.length >= p.EQMAX) {
      return null
    }
    var tmpItem = drawItem()
    p.equip(tmpItem)
    return tmpItem
  }

  def drop(pl: Player, item: Item): Item = {
    return pl.drop(item)
  }

  def beweapon(char: Player, item: Item): Int = {
    if (item == null && char.equipment.length < char.EQMAX) {
      if (char.equippedWeapon.name != "Fist") {
        char.equip(char.equippedWeapon)
        char.equippedWeapon = Weapon("Fist", 0, 1)
        return DISCARD_WEAPON
      } else {
        return CANT_DISCARD_FISTS
      }
    } else if (item == null && char.equippedWeapon.name != "Fist" && char.equipment.length >= char.EQMAX) {
      return CANT_DISCARD_FULL_INV
    } else if (char.equippedWeapon.name != "Fist") {
      char.equipment.append(char.equippedWeapon)
      char.equippedWeapon = item
      char.drop(item)
      return SWAPED_WEAPON
    } else {
      char.equippedWeapon = item
      char.drop(item)
      return EQUIPED_WEAPON
    }
  }

  def attackZombie(pl: Player, z: Zombie): Int = {
    val critRand = util.Random.nextInt(pl.kritchance)
    dmg = pl.attack(critRand)
    lastAttackedZombie = z
    if (z.lifePoints - dmg <= 0) {
      z.lifePoints = 0
      zombies.remove(zombies.indexOf(z))
      zombiesKilled += 1
      if (zombiesKilled >= winCount) {
        return GAME_OVER_WON
      }
      zombieCount -= 1
      z.die()
      return DEAD
    } else {
      z.lifePoints -= dmg
    }
    return PLAYER_ATTACK
  }

  def attackPlayer(pl: Player, z: Zombie): Int = {
    val critRand = util.Random.nextInt(pl.kritchance)
    dmg = z.attack(critRand)
    lastAttackedPlayer = pl
    if (pl.armor != 0) {
      if (pl.armor - dmg <= 0) {
        pl.armor = 0
        return ARMOR_DESTROYED
      } else {
        pl.armor -= dmg
        return ARMOR_DAMAGED
      }
    } else if (pl.lifePoints - dmg <= 0) {
      pl.lifePoints = 0
      val idx = player.indexOf(pl)
      var playerBuf = player.toBuffer
      playerBuf.remove(idx)
      player = playerBuf.toArray
      playerCount -= 1
      if (player.isEmpty) {
        return GAME_OVER_LOST
      }
      pl.die()
      return DEAD
    } else {
      pl.lifePoints -= dmg
    }
    return ZOMBIE_ATTACK
  }

  def attackPlayerPlayer(atk: Player, opf: Player): Int = {
    val critRand = util.Random.nextInt(atk.kritchance)
    dmg = atk.attack(critRand)
    lastAttackedPlayer = opf
    if (opf.armor != 0) {
      if (opf.armor - dmg <= 0) {
        opf.armor = 0
        return ARMOR_DESTROYED
      } else {
        opf.armor -= dmg
        return ARMOR_DAMAGED
      }
    } else if (opf.lifePoints - dmg <= 0) {
      opf.lifePoints = 0
      val idx = player.indexOf(opf)
      var playerBuf = player.toBuffer
      playerBuf.remove(idx)
      player = playerBuf.toArray
      playerCount -= 1
      if (player.isEmpty) {
        return GAME_OVER_LOST
      }
      opf.die()
      return DEAD
    } else {
      opf.lifePoints -= dmg
    }
    return PLAYER_ATTACK_PLAYER
  }

  def attackField(p: Player, f: Field): Int = {
    if (!f.players.isEmpty && p.actualField != f) {
      return attackPlayerPlayer(p, f.players(0))
    } else if (!f.zombies.isEmpty) {
      var az = f.zombies.find { z => z.typ == "Spitter" }
      if (az != None) {
        return attackZombie(p, az.get)
      } else {
        az = f.zombies.find { z => z.typ == "Schlurfer" }
        if (az != None) {
          return attackZombie(p, az.get)
        } else {
          az = f.zombies.find { z => z.typ == "Fatti" }
          if (az != None) {
            return attackZombie(p, az.get)
          } else {
            az = f.zombies.find { z => z.typ == "Tank" }
            if (az != None) {
              return attackZombie(p, az.get)
            } else {
              return attackZombie(p, f.zombies(0))
            }
          }
        }
      }
    } else {
      return FAIL
    }
  }
}