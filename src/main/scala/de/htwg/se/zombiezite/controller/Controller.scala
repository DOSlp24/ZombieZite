package de.htwg.se.zombiezite.controller
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.util.Observable
import scala.collection.mutable.ArrayBuffer
import scala.swing.event.Event
import scala.swing.Publisher

case class GameOverLost() extends Event
case class GameOverWon() extends Event
case class Fail() extends Event
case class ZombieWentUp(typ: String, x: Int, y: Int) extends Event
case class ZombieWentDown(typ: String, x: Int, y: Int) extends Event
case class ZombieWentLeft(typ: String, x: Int, y: Int) extends Event
case class ZombieWentRight(typ: String, x: Int, y: Int) extends Event
case class ZombieAttack(name: String, typ: String, dmg: Int) extends Event
case class DeadPlayer(name: String, murderer: String) extends Event
case class DeadZombie(typ: String, name: String) extends Event
case class DiscardWeapon(w: Item) extends Event
case class CantDiscardFists() extends Event
case class CantDiscardFullInv() extends Event
case class SwappedWeapon() extends Event
case class EquipedWeapon(w: Item) extends Event
case class ArmorDamaged(name: String, typ: String, dmg: Int) extends Event
case class ArmorDestroyed(name: String, typ: String) extends Event
case class PlayerAttack(name: String, typ: String, dmg: Int) extends Event
case class PlayerAttackPlayer(atk: String, opf: String, dmg: Int) extends Event
case class ZombieDraw(z: Array[Zombie]) extends Event
case class Wait(p: Player) extends Event
case class AktivierungZombies() extends Event
case class AktivierungRunner() extends Event
case class DrawZombie() extends Event
case class NewRound(round: Int) extends Event
case class StartSpieler() extends Event
case class StartSchwierig() extends Event
case class Start() extends Event
case class PlayerMove(x: Int, y: Int) extends Event
case class Search(i: Item) extends Event
case class WaitInput() extends Event

class Controller() extends Publisher {

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
  var winCount = 50
  var round = 1
  var actualPlayer = Player(area, "Default")

  def setDifficulty(dif: Int) {
    winCount = dif * playerCount.toInt * 15
    publish(new Start)
  }
  
  def waitInput() {
    publish(new WaitInput)
  }

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
    publish(new StartSchwierig)
    roundReset()
  }

  def newRound {
    round += 1
    publish(new NewRound(round))
  }

  def wait(p: Player) {
    p.actionCounter = 0
    publish(Wait(p))
    if(actualPlayer != player.last){
      actualPlayer = player(player.indexOf(actualPlayer) + 1)
      return
    } else {
      fullZombieTurn
      roundReset
      newRound
    }
  }

  def roundReset() {
    player.foreach { p => p.actionCounter = 3 }
    actualPlayer = player(0)
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
    publish(ZombieDraw(tempZombie))
    return tempZombie
  }

  def fullZombieTurn {
    zombies.foreach { z => zombieTurn(z) }
    publish(new AktivierungZombies)
    zombies.foreach { z => if (z.name == "Runner") { zombieTurn(z) } }
    publish(new AktivierungRunner)
    drawZombie()
    publish(new DrawZombie)
  }

  def zombieTurn(z: Zombie) {
    player.foreach { j =>
      if (z.actualField.p.x == j.actualField.p.x) {
        if (math.abs(z.actualField.p.y - j.actualField.p.y) <= z.range) {
          attackPlayer(j, z)
          return
        } else if (z.actualField.p.y < j.actualField.p.y) {
          z.walk(0, 1)
          publish(new ZombieWentUp(z.name, z.actualField.p.x / fieldlength, z.actualField.p.y / fieldlength))
          return
          //return ZOMBIE_WENT_UP
        } else if (z.actualField.p.y > j.actualField.p.y) {
          z.walk(0, -1)
          publish(new ZombieWentDown(z.name, z.actualField.p.x / fieldlength, z.actualField.p.y / fieldlength))
          return
          //return ZOMBIE_WENT_DOWN
        }
      } else if (z.actualField.p.y == j.actualField.p.y) {
        if (math.abs(z.actualField.p.x - j.actualField.p.x) <= z.range) {
          attackPlayer(j, z)
          return
        } else if (z.actualField.p.x < j.actualField.p.x) {
          z.walk(1, 0)
          publish(new ZombieWentRight(z.name, z.actualField.p.x / fieldlength, z.actualField.p.y / fieldlength))
          return
          //return ZOMBIE_WENT_RIGHT
        } else if (z.actualField.p.x > j.actualField.p.x) {
          z.walk(-1, 0)
          publish(new ZombieWentLeft(z.name, z.actualField.p.x / fieldlength, z.actualField.p.y / fieldlength))
          return
          //return ZOMBIE_WENT_LEFT
        }
      }
    }
    if (z.walk(-1, 0)) {
      publish(new ZombieWentLeft(z.name, z.actualField.p.x / fieldlength, z.actualField.p.y / fieldlength))
      return
      //return ZOMBIE_WENT_LEFT
    } else if (z.walk(0, 1)) {
      publish(new ZombieWentUp(z.name, z.actualField.p.x / fieldlength, z.actualField.p.y / fieldlength))
      return
      //return ZOMBIE_WENT_UP
    } else {
      publish(new ZombieWentUp(z.name, z.actualField.p.x / fieldlength, z.actualField.p.y / fieldlength))
      return
      //return ZOMBIE_WENT_RIGHT
    }
  }

  def move(char: Character, x: Int, y: Int): Boolean = {
    if (char.walk(x, y)) {
      char.actionCounter -= 1
      publish(new PlayerMove(char.actualField.p.x / fieldlength, char.actualField.p.y / fieldlength))
      return true
    } else {
      return false
    }
  }

  def search(p: Player){
    if (p.equipment.length >= p.EQMAX) {
      publish(new Search(null))
      return
    }
    var tmpItem = drawItem()
    p.equip(tmpItem)
    p.actionCounter -= 1
    publish(new Search(tmpItem))
    return
  }

  def drop(pl: Player, item: Item): Item = {
    return pl.drop(item)
  }

  def beweapon(char: Player, item: Item) = {
    if (item == null && char.equipment.length < char.EQMAX) {
      if (char.equippedWeapon.name != "Fist") {
        val tmp = char.equippedWeapon
        char.equip(tmp)
        char.equippedWeapon = Weapon("Fist", 0, 1)
        publish(new DiscardWeapon(tmp))
        //return DISCARD_WEAPON
      } else {
        publish(new CantDiscardFists)
        //return CANT_DISCARD_FISTS
      }
    } else if (item == null && char.equippedWeapon.name != "Fist" && char.equipment.length >= char.EQMAX) {
      publish(new CantDiscardFullInv)
      //return CANT_DISCARD_FULL_INV
    } else if (char.equippedWeapon.name != "Fist") {
      char.equipment.append(char.equippedWeapon)
      char.equippedWeapon = item
      char.drop(item)
      publish(new SwappedWeapon)
      //return SWAPED_WEAPON
    } else {
      char.equippedWeapon = item
      char.drop(item)
      publish(new EquipedWeapon(char.equippedWeapon))
      //return EQUIPED_WEAPON
    }
  }

  def attackZombie(pl: Player, z: Zombie) = {
    pl.actionCounter -= 1
    val critRand = util.Random.nextInt(pl.kritchance)
    val dmg = pl.attack(critRand)
    if (z.lifePoints - dmg <= 0) {
      z.lifePoints = 0
      zombies.remove(zombies.indexOf(z))
      zombiesKilled += 1
      if (zombiesKilled >= winCount) {
        publish(new GameOverWon)
        //return GAME_OVER_WON
      }
      zombieCount -= 1
      z.die()
      publish(new DeadZombie(z.name, pl.name))
      //return DEAD
    } else {
      z.lifePoints -= dmg
    }
    publish(new PlayerAttack(pl.name, z.name, dmg))
    //return PLAYER_ATTACK
  }

  def attackPlayer(pl: Player, z: Zombie) = {
    val critRand = util.Random.nextInt(pl.kritchance)
    val dmg = z.attack(critRand)
    if (pl.armor != 0) {
      if (pl.armor - dmg <= 0) {
        pl.armor = 0
        publish(new ArmorDestroyed(pl.name, z.name))
        //return ARMOR_DESTROYED
      } else {
        pl.armor -= dmg
        publish(new ArmorDamaged(pl.name, z.name, dmg))
        //return ARMOR_DAMAGED
      }
    } else if (pl.lifePoints - dmg <= 0) {
      pl.lifePoints = 0
      val idx = player.indexOf(pl)
      var playerBuf = player.toBuffer
      playerBuf.remove(idx)
      player = playerBuf.toArray
      playerCount -= 1
      if (player.isEmpty) {
        publish(new DeadPlayer(pl.name, z.name))
        publish(new GameOverLost)
        //return GAME_OVER_LOST
      }
      pl.die()
      publish(new DeadPlayer(pl.name, z.name))
      //return DEAD
    } else {
      pl.lifePoints -= dmg
    }
    publish(new ZombieAttack(pl.name, z.name, dmg))
    //return ZOMBIE_ATTACK
  }

  def attackPlayerPlayer(atk: Player, opf: Player) = {
    atk.actionCounter -= 1
    val critRand = util.Random.nextInt(atk.kritchance)
    val dmg = atk.attack(critRand)
    if (opf.armor != 0) {
      if (opf.armor - dmg <= 0) {
        opf.armor = 0
        publish(new ArmorDestroyed(opf.name, atk.name))
        //return ARMOR_DESTROYED
      } else {
        opf.armor -= dmg
        publish(new ArmorDamaged(opf.name, atk.name, dmg))
        //return ARMOR_DAMAGED
      }
    } else if (opf.lifePoints - dmg <= 0) {
      opf.lifePoints = 0
      val idx = player.indexOf(opf)
      var playerBuf = player.toBuffer
      playerBuf.remove(idx)
      player = playerBuf.toArray
      playerCount -= 1
      if (player.isEmpty) {
        publish(new DeadPlayer(atk.name, opf.name))
        publish(new GameOverLost)
        //return GAME_OVER_LOST
      }
      opf.die()
      publish(new DeadPlayer(opf.name, atk.name))
      //return DEAD
    } else {
      opf.lifePoints -= dmg
    }
    publish(new PlayerAttackPlayer(atk.name, opf.name, dmg))
    //return PLAYER_ATTACK_PLAYER
  }

  def attackField(p: Player, f: Field) = {
    p.actionCounter -= 1
    if (!f.players.isEmpty && p.actualField != f) {
      p.actionCounter += 1
      attackPlayerPlayer(p, f.players(0))
    } else if (!f.zombies.isEmpty) {
      var az = f.zombies.find { z => z.name == "Spitter" }
      if (az != None) {
        p.actionCounter += 1
        attackZombie(p, az.get)
      } else {
        az = f.zombies.find { z => z.name == "Schlurfer" }
        if (az != None) {
          p.actionCounter += 1
          attackZombie(p, az.get)
        } else {
          az = f.zombies.find { z => z.name == "Fatti" }
          if (az != None) {
            p.actionCounter += 1
            attackZombie(p, az.get)
          } else {
            az = f.zombies.find { z => z.name == "Tank" }
            if (az != None) {
              p.actionCounter += 1
              attackZombie(p, az.get)
            } else {
              p.actionCounter += 1
              attackZombie(p, f.zombies(0))
            }
          }
        }
      }
    } else {
      publish(new Fail)
      //return FAIL
    }
  }
}