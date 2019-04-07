package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model
import de.htwg.se.zombiezite.model.baseImpl._
import de.htwg.se.zombiezite.model.{ PlayerInterface, ZombieInterface, _ }

import scala.collection.mutable.ArrayBuffer
import scala.swing.Publisher
import scala.swing.event.Event

//noinspection ScalaStyle
class FController() extends Publisher with FControllerInterface {

  case class cState(
      dif: Int = 2,
      player: Vector[FPlayerInterface] = Vector[FPlayerInterface](),
      zombies: Vector[FZombieInterface] = Vector[FZombieInterface](),
      playerCount: Int = 0,
      actualPlayer: FPlayerInterface = FPlayerWithoutIdentity(),
      area: FAreaInterface = FArea(10, 10).build(),
      round: Int = 0,
      winCount: Int = 60
  ) {

    def updateChars(): cState = {
      copy(zombies = searchLinesForZombies(), player = searchLinesForPlayers())
    }

    def updateAreaOverChar(index: Int = 0, chars: Vector[FCharacterInterface] = zombies ++ player): cState = {
      if (index == chars.length - 1) {
        buildArea().enterField(chars(index))
      } else {
        updateAreaOverChar(index + 1, chars).enterField(chars(index))
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
      copy(area = FArea(area.len, area.wid).build())
    }

    def enterField(c: FCharacterInterface): cState = {
      c match {
        case interface: FPlayerInterface => enterFieldPlayer(c.asInstanceOf[FPlayerInterface])
        case interface: FZombieInterface => enterFieldZombie(c.asInstanceOf[FZombieInterface])
      }
    }

    def enterFieldPlayer(p: FPlayerInterface): cState = {
      player.length match {
        case 0 => copy(actualPlayer = p, area = area.putField(area.lines(p.y)(p.x).enterField(p))).updateChars()
        case _ => copy(area = area.putField(area.lines(p.y)(p.x).enterField(p))).updateChars()
      }
    }

    def enterFieldZombie(z: FZombieInterface): cState = {
      copy(area = area.putField(area.lines(z.y)(z.x).enterField(z))).updateChars()
    }

    def leaveField(c: FCharacterInterface): cState = {
      copy(area = area.putField(area.lines(c.y)(c.x).leaveField(c))).updateChars()
    }

    def moveUp(c: FCharacterInterface): cState = {
      leaveField(c).enterField(c.walk(0, -1)).updateChars()
    }

    def moveDown(c: FCharacterInterface): cState = {
      leaveField(c).enterField(c.walk(0, 1)).updateChars()
    }

    def moveLeft(c: FCharacterInterface): cState = {
      leaveField(c).enterField(c.walk(-1, 0)).updateChars()
    }

    def moveRight(c: FCharacterInterface): cState = {
      leaveField(c).enterField(c.walk(1, 0)).updateChars()
    }

    def increaseRoundCount(): cState = {
      copy(round = round + 1)
    }

    def nextPlayer(): cState = {
      val index = player.indexOf(actualPlayer)
      if (index < player.length - 1) {
        copy(actualPlayer = player(index + 1))
      } else {
        copy(actualPlayer = player(0)).startNewTurn()
      }
    }

    def startNewTurn(): cState = {
      increaseRoundCount().zombieTurn()
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

      val newZombies: Vector[FZombieInterface] = zombies.map(z => execTurn(z))
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
      //p.takeDmg(z.equippedWeapon.strength * z.strength) //TODO Attack a player should be a variable in FZombieInterface
    }

    def zombiesTriggerAttack(z: FZombieInterface = zombies.apply(0)): cState = {
      val archenemy = z.archenemy
      if (z == zombies.last) {
        if (player.contains(archenemy)) {
          copy(player = player.updated(player.indexOf(archenemy), archenemy.takeDmg(z.equippedWeapon.strength * z.strength)))
        } else {
          this
        }
      } else {
        if (player.contains(archenemy)) {
          zombiesTriggerAttack(zombies.apply(zombies.indexOf(z) + 1)).copy(player = player.updated(player.indexOf(archenemy), archenemy.takeDmg(z.equippedWeapon.strength * z.strength)))
        } else {
          zombiesTriggerAttack(zombies.apply(zombies.indexOf(z) + 1))
        }
      }
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
