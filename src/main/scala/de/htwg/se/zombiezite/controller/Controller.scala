package de.htwg.se.zombiezite.controller
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.util.Observable
import scala.collection.mutable.ArrayBuffer

class Controller() extends Observable {

  var area: Area = null
  var player: Array[Player] = null
  var zombies: ArrayBuffer[Zombie] = ArrayBuffer[Zombie]()
  var itemDeck: Deck[Item] = new ItemDeck()
  var zombieDeck: Deck[Array[Zombie]] = null
  val playerNamer: Array[String] = Array("Franz Maiar", "Kohsuke Kawaguchi", "Hasso Kaiba", "Petal Blossom Rainbow")

  def init(playerCount: Int) {
    player = new Array[Player](playerCount)
    area = new Area(10, 10)
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
    return itemDeck.draw()
  }

  def drawZombie(): Array[Zombie] = {
    var tempZombie = zombieDeck.draw()
    if (!tempZombie.isEmpty) {
      for (i <- 0 to tempZombie.length - 1) {
        println("Zombie gezogen: " + tempZombie(i).typ)
        zombies.append(tempZombie(i))
      }
    }
    return tempZombie
  }

  def playerPos(): String = {
    var s = ""
    player.foreach { p => s += p.name + "(" + p.actualField.p.x / 2 + "," + p.actualField.p.y / 2 + ")" + " [" + p.lifePoints + " LP]\n" }
    s += "\n"
    return s
  }

  def zombiePos(): String = {
    var s = ""
    if (zombies.isEmpty) {
      s += "*Keine Zombies auf dem Spielfeld!*\n\n"
      return s
    }
    for (z <- 0 to zombies.length - 1) {
      s += zombies(z).typ + "(" + zombies(z).actualField.p.x / 2 + "," + zombies(z).actualField.p.y / 2 + ")" + " [" + zombies(z).lifePoints + " LP]\n"
    }
    s += "\n"
    return s
  }

  def zombieTurn(z: Zombie): Boolean = {
    for (j <- 0 to player.length - 1) {

      if (z.actualField.p.x == player(j).actualField.p.x) {

        if (z.actualField.p.y == player(j).actualField.p.y) {
          return attackPlayer(player(j), z)
        } else if (z.actualField.p.y < player(j).actualField.p.y) {
          z.walk(0, 1)
          println("Ein " + z.typ + " ist ein Feld nach oben gelaufen.       Auf Feld(" + z.actualField.p.x / 2 + ", " + z.actualField.p.y / 2 + ")")
          return true
        } else if (z.actualField.p.y > player(j).actualField.p.y) {
          z.walk(0, -1)
          println("Ein " + z.typ + " ist ein Feld nach unten gelaufen.       Auf Feld(" + z.actualField.p.x / 2 + ", " + z.actualField.p.y / 2 + ")")
          return true
        }
      } else if (z.actualField.p.y == player(j).actualField.p.y) {
        if (z.actualField.p.x < player(j).actualField.p.x) {
          z.walk(1, 0)
          println("Ein " + z.typ + " ist ein Feld nach rechts gelaufen.       Auf Feld(" + z.actualField.p.x / 2 + ", " + z.actualField.p.y / 2 + ")")
          return true
        } else if (z.actualField.p.x > player(j).actualField.p.x) {
          z.walk(-1, 0)
          println("Ein " + z.typ + " ist ein Feld nach links gelaufen.       Auf Feld(" + z.actualField.p.x / 2 + ", " + z.actualField.p.y / 2 + ")")
          return true
        }
      }
    }
    if (z.walk(-1, 0)) {
      println("Ein " + z.typ + " ist ein Feld nach links gelaufen.       Auf Feld(" + z.actualField.p.x / 2 + ", " + z.actualField.p.y / 2 + ")")
      return true
    } else if (z.walk(0, 1)) {
      println("Ein " + z.typ + " ist ein Feld nach oben gelaufen.       Auf Feld(" + z.actualField.p.x / 2 + ", " + z.actualField.p.y / 2 + ")")
      return true
    } else if (z.walk(1, 0)) {
      println("Ein " + z.typ + " ist ein Feld nach rechts gelaufen.       Auf Feld(" + z.actualField.p.x / 2 + ", " + z.actualField.p.y / 2 + ")")
    }
    return true
  }

  def move(char: Character, x: Int, y: Int): Boolean = {
    return char.walk(x, y)
  }

  def search(p: Player): Boolean = {
    if (p.equipment.length >= p.EQMAX) {
      return false
    }
    var tmpItem = drawItem()
    println("Du hast folgendes gefunden: " + tmpItem)
    return p.equip(tmpItem)
  }

  def drop(pl: Player, item: Item) {
    pl.drop(item)
  }

  def equip(pl: Player) {
    pl.printEq()
  }

  def beweapon(char: Player, item: Item) {
    if (item == null && char.equipment.length < char.EQMAX) {
      if (char.equippedWeapon.name != "Fist") {
        println("Du hast *" + char.equippedWeapon.name + "* abgelegt")
        char.equip(char.equippedWeapon)
        char.equippedWeapon = Weapon("Fist", 0, 1)
        println("Ab jetzt kämpft " + char.name + " nur noch mit seinen bloßen Fäusten.")
      } else {
        println("Fäuste kannst du nicht ablegen!")
      }
      return
    } else if (char.equippedWeapon.name != "Fist" && char.equipment.length >= char.EQMAX) {
      println("Dein Rucksack ist voll. Du kannst  *" + char.equippedWeapon.name + "*  nicht mehr hineinquetschen!")
      return
    } else if (char.equippedWeapon.name != "Fist") {
      char.equipment.append(char.equippedWeapon)
      char.equippedWeapon = item
      char.drop(item)
      println("Du hast die Waffen getauscht")
    } else {
      char.equippedWeapon = item
      char.drop(item)
      println("Du hast *" + char.equippedWeapon.name + "* ausgerüstet.")
    }
  }

  def attackZombie(pl: Player, z: Zombie): Int = {
    val dmg = pl.attack()
    println("\n" + pl.name + " verursachte [" + dmg + "] Schaden an einem " + z.typ + "\n")
    if (z.lifePoints - dmg <= 0) {
      z.lifePoints = 0
      println(z.die())
      zombies.remove(zombies.indexOf(z))
    } else {
      z.lifePoints -= dmg
    }
    return 1
  }

  def attackPlayer(pl: Player, z: Zombie): Boolean = {
    val dmg = z.attack()
    println("\nEin " + z.typ + " verursachte [" + dmg + "] Schaden an " + pl.name + "\n")
    if (pl.lifePoints - dmg <= 0) {
      pl.lifePoints = 0
      println(pl.die())
      val idx = player.indexOf(pl)
      var playerBuf = player.toBuffer
      playerBuf.remove(idx)
      player = playerBuf.toArray
      if (player.isEmpty) {
        println("\n\n\n|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|")
        println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
        println(":_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:::::Game Over:::::-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
        println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
        println("|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`")
        return false
      }
    } else {
      pl.lifePoints -= dmg
    }
    return true
  }

  def attackField(char: Character, f: Field) {
    //TODO implement how a character attacks a whole field
  }
}