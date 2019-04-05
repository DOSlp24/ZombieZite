package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model
import de.htwg.se.zombiezite.model.baseImpl.{Area, FArea, FZombie, Zombie}
import de.htwg.se.zombiezite.model.{PlayerInterface, ZombieInterface, _}

import scala.swing.Publisher
import scala.swing.event.Event

class FController() extends Publisher with ControllerInterface {

  case class cState(
                     dif: Int = 2,
                     player: Vector[FPlayerInterface],
                     zombies: Vector[FZombieInterface],
                     playerCount: Int,
                     actualPlayer: FPlayerInterface,
                     area: FAreaInterface = FArea(10, 10),
                     round: Int = 0,
                     winCount: Int = 60
                   ) {

    def updateChars(): cState = {
      copy(zombies = searchLinesForZombies(), player = searchLinesForPlayers())
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
        searchFieldForZombies(line, field + 1) ++ area.lines(line)(field).zombies
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
        searchFieldForPlayers(line, field + 1) ++ area.lines(line)(field).players
      } else {
        Vector[FPlayerInterface]()
      }
    }

    def buildArea(): cState = {
      copy(area = area.build)
    }

    def enterField(c: FCharacterInterface): cState = {
      copy(area = area.putField(area.lines.apply(c.y).apply(c.x).enterField(c)))
    }

    def leaveField(c: FCharacterInterface): cState = {
      copy(area = area.putField(area.lines.apply(c.y).apply(c.x).leaveField(c)))
    }

    def moveUp(c: FCharacterInterface): cState = {
      leaveField(c)
      enterField(c.walk(0, 1))
    }

    def moveDown(c: FCharacterInterface): cState = {
      leaveField(c)
      enterField(c.walk(0, -1))
    }

    def moveLeft(c: FCharacterInterface): cState = {
      leaveField(c)
      enterField(c.walk(-1, 0))
    }

    def moveRight(c: FCharacterInterface): cState = {
      leaveField(c)
      enterField(c.walk(1, 0))
    }
  }

  def startNewRound(state: cState): cState = {
    val actualPlayer = nextPlayer(state.actualPlayer, state.player)
    val round = state.round + 1

    publish(NewRound(round))

    cState(state.dif, state.player, state.zombies, state.playerCount, actualPlayer, state.area, round, state.winCount)
  }

  def nextPlayer(actualPlayer: FPlayerInterface, player: Vector[FPlayerInterface]): FPlayerInterface = {
    val index = player.indexOf(actualPlayer)
    if (index < player.length - 1) {
      player(index + 1)
    } else {
      player(0)
    }
  }

  override def checkOrder: Unit = ???

  override def setDifficulty(dif: Int): Unit = ???

  override def waitInput(): Unit = ???

  override def init(playerCounter: Int): Unit = ???

  override def newRound: Unit = ???

  override def wait(p: PlayerInterface): Unit = ???

  override def roundReset(): Unit = ???

  override def drawItem(): Item = ???

  override def attackableFields(char: model.Character): Array[FieldInterface] = ???

  override def availableWeapon(p: PlayerInterface): Array[Item] = ???

  override def drawZombie(): Array[ZombieInterface] = ???

  override def fullZombieTurn: Unit = ???

  override def zombieTurn(z: ZombieInterface): Unit = ???

  override def move(char: model.Character, x: Int, y: Int): Unit = ???

  override def search(p: PlayerInterface): Unit = ???

  override def drop(pl: PlayerInterface, item: Item): Unit = ???

  override def equipArmor(char: PlayerInterface, i: ArmorInterface): Unit = ???

  override def beweapon(char: PlayerInterface, item: WeaponInterface): Unit = ???

  override def attackZombie(pl: PlayerInterface, z: ZombieInterface): Unit = ???

  override def attackPlayer(pl: PlayerInterface, z: ZombieInterface): Unit = ???

  override def attackPlayerPlayer(atk: PlayerInterface, opf: PlayerInterface): Unit = ???

  override def attackField(p: PlayerInterface, f: FieldInterface): Unit = ???

  override def attackWholeField(p: PlayerInterface, f: FieldInterface): Boolean = ???

  override def getZombieList: Array[String] = ???

  override def getItemList: Array[String] = ???
}
