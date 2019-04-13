package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model
import de.htwg.se.zombiezite.model.baseImpl._
import de.htwg.se.zombiezite.model.{ PlayerInterface, ZombieInterface, _ }

import scala.collection.mutable.ArrayBuffer
import scala.swing.Publisher
import scala.swing.event.Event

case class Update(state: cState) extends Event

//noinspection ScalaStyle
case class cState(
    dif: Int = 2,
    player: Vector[FPlayerInterface] = Vector[FPlayerInterface](),
    zombies: Vector[FZombieInterface] = Vector[FZombieInterface](),
    playerCount: Int = 0,
    actualPlayer: Int = 0,
    area: FAreaInterface = FArea(10, 10).build(),
    round: Int = 0,
    winCount: Int = 60,
    zombiesKilled: Int = 0,
    zombieDeck: FDeckInterface = FZombieDeck(),
    itemDeck: FDeckInterface = FItemDeck().shuffle(),
    playerOrder: Vector[String] = Vector[String]("F. Maiar", "K. Kawaguchi", "H. Kaiba", "P. B. Rainbow")
) {

  def updateChars(): cState = {
    copy(zombies = searchLinesForZombies(), player = searchLinesForPlayers().sortWith((p1, p2) => playerOrder.indexOf(p1.name) < playerOrder.indexOf(p2.name))).checkActionCounter()
  }

  def updateAreaOverChar(index: Int = 0, chars: Vector[FCharacterInterface] = zombies ++ player): cState = {
    if (index == chars.length - 1) {
      buildArea().enterField(chars(index)).checkActionCounter()
    } else {
      updateAreaOverChar(index + 1, chars).enterField(chars(index)).checkActionCounter()
    }
  }

  def pushActualPlayer(newActualPlayer: FPlayerInterface): cState = {
    copy(player = player.updated(actualPlayer, newActualPlayer)).checkActionCounter()
  }

  def checkActionCounter(): cState = {
    if (player.length > actualPlayer) {
      val actionCounter = player(actualPlayer).actionCounter
      actionCounter match {
        case a if a > 0 => this
        case _ => nextPlayer()
      }
    } else {
      this
    }
  }

  def searchLinesForZombies(line: Int = 0): Vector[FZombieInterface] = {
    if (line < area.len) {
      searchLinesForZombies(line + 1) ++ searchFieldForZombies(line, 0)
    } else {
      Vector[FZombieInterface]()
    }
  }

  def searchFieldForZombies(line: Int, field: Int): Vector[FZombieInterface] = {
    if (field < area.wid) {
      searchFieldForZombies(line, field + 1) ++ area.lines(line)(field).zombies.filter(z => z.lifePoints > 0)
    } else {
      Vector[FZombieInterface]()
    }
  }

  def searchLinesForPlayers(line: Int = 0): Vector[FPlayerInterface] = {
    if (line < area.len) {
      searchLinesForPlayers(line + 1) ++ searchFieldForPlayers(line, 0)
    } else {
      Vector[FPlayerInterface]()
    }
  }

  def searchFieldForPlayers(line: Int, field: Int): Vector[FPlayerInterface] = {
    if (field < area.wid) {
      searchFieldForPlayers(line, field + 1) ++ area.lines(line)(field).players.filter(p => p.lifePoints > 0)
    } else {
      Vector[FPlayerInterface]()
    }
  }

  def buildArea(): cState = {
    copy(area = FArea(area.len, area.wid).build())
  }

  def enterFieldMulti(index: Int = 0, chars: Vector[FCharacterInterface]): cState = {
    if (index < chars.length) {
      enterFieldMulti(index = index + 1, chars).enterField(chars(index))
    } else {
      this
    }
  }

  def enterField(c: FCharacterInterface): cState = {
    c match {
      case interface: FPlayerInterface => enterFieldPlayer(c.asInstanceOf[FPlayerInterface])
      case interface: FZombieInterface => enterFieldZombie(c.asInstanceOf[FZombieInterface])
    }
  }

  def enterFieldPlayer(p: FPlayerInterface): cState = {
    player.length match {
      case 0 => copy(actualPlayer = 0, area = area.putField(area.lines(p.y)(p.x).enterField(p))).updateChars()
      case _ => copy(area = area.putField(area.lines(p.y)(p.x).enterField(p))).updateChars()
    }
  }

  def enterFieldZombie(z: FZombieInterface): cState = {
    copy(area = area.putField(area.lines(z.y)(z.x).enterField(z))).updateChars()
  }

  def leaveField(c: FCharacterInterface): cState = {
    copy(area = area.putField(area.lines(c.y)(c.x).leaveField(c)))
  }

  def moveUp(c: FCharacterInterface): cState = {
    player(actualPlayer).y match {
      case y if y > 0 => leaveField(c).enterField(c.walk(0, -1))
      case _ => this
    }
  }

  def moveDown(c: FCharacterInterface): cState = {
    player(actualPlayer).y match {
      case y if y < area.len - 1 => leaveField(c).enterField(c.walk(0, 1))
      case _ => this
    }
  }

  def moveLeft(c: FCharacterInterface): cState = {
    player(actualPlayer).x match {
      case x if x > 0 => leaveField(c).enterField(c.walk(-1, 0))
      case _ => this
    }
  }

  def moveRight(c: FCharacterInterface): cState = {
    player(actualPlayer).x match {
      case x if x < area.wid - 1 => leaveField(c).enterField(c.walk(1, 0))
      case _ => this
    }
  }

  def pWait(): cState = {
    nextPlayer()
  }

  def drawItem(): cState = {
    val drawnItem = itemDeck.asInstanceOf[FItemDeck].draw()
    val newActualPlayer = player(actualPlayer).takeItem(drawnItem)
    copy(itemDeck = itemDeck.asInstanceOf[FItemDeck].afterDraw(), player = player.updated(actualPlayer, newActualPlayer))
  }

  def dropItem(i: FItemInterface): cState = {
    val newActualPlayer = player(actualPlayer).drop(i)
    pushActualPlayer(newActualPlayer)
  }

  def equipWeapon(w: FWeaponInterface): cState = {
    val newActualPlayer = player(actualPlayer).equipWeapon(w)
    pushActualPlayer(newActualPlayer)
  }

  def useArmor(a: FArmorInterface): cState = {
    val newActualPlayer = player(actualPlayer).useArmor(a)
    pushActualPlayer(newActualPlayer)
  }

  def drawZombie(): cState = {
    val zombiesDrawn = zombieDeck.asInstanceOf[FZombieDeck].draw()
    enterFieldMulti(chars = zombiesDrawn).updateChars()
  }

  def increaseRoundCount(): cState = {
    copy(round = round + 1)
  }

  def nextPlayer(): cState = {
    val actual = player(actualPlayer)
    if (actual.name != playerOrder.last) {
      copy(actualPlayer = actualPlayer + 1, player = player.updated(actualPlayer + 1, player(actualPlayer + 1).resetActionCounter))
    } else {
      copy(actualPlayer = 0, player = player.updated(0, player(0).resetActionCounter)).startNewTurn()
    }
  }

  def startNewTurn(): cState = {
    increaseRoundCount().zombieTurn().drawZombie()
  }

  def zombieTurn(): cState = {
    def execTurn(z: FZombieInterface): FZombieInterface = {
      val canSeePlayer = player.filter(p => {
        p.x == z.x || p.y == z.y
      })

      if (canSeePlayer.nonEmpty) {

        val canAttackPlayer = canSeePlayer.filter(p => {
          math.abs(z.y - p.y) + math.abs(z.x - p.x) <= z.range
        })

        if (canAttackPlayer.nonEmpty) {
          z.selectTarget(canAttackPlayer(0))
        } else {
          zombieMoveTowards(canSeePlayer.apply(0), z)
        }
      } else {
        zombieMove(z)
      }
    }

    val newZombies: Vector[FZombieInterface] = zombies.map {
      case z if z.name == "Runner" => execTurn(execTurn(z)) // Runner trigger twice
      case z => execTurn(z)
    }
    val ret = copy(zombies = newZombies)
    ret.zombies.length match {
      case 0 => ret.updateAreaOverChar().updateChars()
      case _ => ret.zombiesTriggerAttack().updateAreaOverChar().updateChars()
    }
  }

  def executeZombieTurn(z: FZombieInterface): FZombieInterface = {
    val canSeePlayer = player.filter(p => {
      p.x == z.x || p.y == z.y
    })

    if (canSeePlayer.nonEmpty) {

      val canAttackPlayer = canSeePlayer.filter(p => {
        math.abs(z.y - p.y) <= z.range || math.abs(z.x - p.x) <= z.range
      })

      if (canAttackPlayer.nonEmpty) {
        zombieAttack(z, canAttackPlayer.apply(0))
      } else {
        zombieMoveTowards(canSeePlayer.apply(0), z)
      }
    } else {
      zombieMove(z)
    }
  }

  def zombieMove(z: FZombieInterface): FZombieInterface = {
    z match {
      case _ if z.x == area.wid - 1 => z.walk(-1, 0)
      case _ if z.x == 0 => z.walk(1, 0)
      case _ if z.y == area.len - 1 => z.walk(0, -1)
      case _ if z.y == 0 => z.walk(0, 1)
      case _ => {
        val random = scala.util.Random.nextInt(4)
        random match {
          case 0 => z.walk(0, 1)
          case 1 => z.walk(0, -1)
          case 2 => z.walk(1, 0)
          case 3 => z.walk(-1, 0)
        }
      }
    }
  }

  def zombieMoveTowards(p: FPlayerInterface, z: FZombieInterface): FZombieInterface = {
    //Move towards player

    val x = p.x - z.x
    val y = p.y - z.y

    if (x < 0) {
      z.walk(-1, 0)
    } else if (x > 0) {
      z.walk(1, 0)
    } else {
      if (y < 0) {
        z.walk(0, -1)
      } else if (y > 0) {
        z.walk(0, 1)
      } else {
        z
      }
    }
  }

  def zombieAttack(z: FZombieInterface, p: FPlayerInterface): FZombieInterface = {
    z.selectTarget(p)
    //p.takeDmg(z.equippedWeapon.strength * z.strength)
  }

  def zombiesTriggerAttack(): cState = {
    val kawaDmg: Int = zombies.filter(zombie => zombie.archenemy.name.contains("Kawaguchi")).map(z => z.strength).sum
    val maierDmg = zombies.filter(zombie => zombie.archenemy.name.contains("Maiar")).map(z => z.strength).sum
    val rainDmg = zombies.filter(zombie => zombie.archenemy.name.contains("Rainbow")).map(z => z.strength).sum
    val kaibaDmg = zombies.filter(zombie => zombie.archenemy.name.contains("Kaiba")).map(z => z.strength).sum
    println(kawaDmg)
    println(maierDmg)
    println(rainDmg)
    println(kaibaDmg)
    this
  }
}

//noinspection ScalaStyle
class FController() extends Publisher with FControllerInterface {

  override def init(): cState = {
    val p1 = FPlayer(name = "F. Maiar", x = 5, y = 0)
    val p2 = FPlayer(name = "K. Kawaguchi", x = 5, y = 0)
    val p3 = FPlayer(name = "H. Kaiba", x = 5, y = 0)
    val p4 = FPlayer(name = "P. B. Rainbow", x = 5, y = 0)
    val retState = cState().buildArea().enterField(p1).enterField(p2).enterField(p3).enterField(p4)
    publish(Update(retState))
    retState
  }

  def startNewRound(state: cState): cState = {
    val actualPlayer = nextPlayer(state)
    val round = state.round + 1

    val retState = cState(actualPlayer = actualPlayer, round = round)
    publish(Update(retState))
    retState
  }

  def nextPlayer(state: cState): Int = {
    val actualPlayer = state.actualPlayer
    val lastPlayer = state.player.length - 1
    actualPlayer match {
      case _ if actualPlayer == lastPlayer => 0
      case _ => actualPlayer + 1
    }
  }

  override def wait(state: cState): cState = {
    val retState = state.pWait
    publish(Update(retState))
    retState
  }

  override def move(state: cState, direction: String): cState = {
    val retState: cState = direction match {
      case "up" => state.moveUp(state.player(state.actualPlayer))
      case "down" => state.moveDown(state.player(state.actualPlayer))
      case "left" => state.moveLeft(state.player(state.actualPlayer))
      case "right" => state.moveRight(state.player(state.actualPlayer))
    }
    publish(Update(retState))
    retState
  }

  override def search(state: cState): cState = {
    val retState = state.drawItem()
    publish(Update(retState))
    retState
  }

  override def drop(state: cState, item: FItemInterface): cState = {
    val retState = state.dropItem(item)
    publish(Update(retState))
    retState
  }

  override def equipArmor(state: cState, i: FArmorInterface): cState = {
    val retState = state.useArmor(i)
    publish(Update(retState))
    retState
  }

  override def beweapon(state: cState, weapon: FWeaponInterface): cState = {
    val retState = state.equipWeapon(weapon)
    publish(Update(retState))
    retState
  }

  override def triggerAttack(state: cState): cState = {
    state
  }

  override def attackField(state: cState, f: FieldInterface): cState = {
    state
  }
}
