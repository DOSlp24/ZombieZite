package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model
import de.htwg.se.zombiezite.model.baseImpl.{Area, Zombie}
import de.htwg.se.zombiezite.model.{PlayerInterface, ZombieInterface, _}

import scala.swing.Publisher
import scala.swing.event.Event

class FController() extends Publisher with ControllerInterface {

  case class cState(
                     dif: Int,
                     player: Array[PlayerInterface],
                     zombies: Array[ZombieInterface],
                     playerCount: Int,
                     actualPlayer: PlayerInterface,
                     area: AreaInterface,
                     round: Int,
                     winCount: Int
                   ) {
    def buildArea(): cState = {
      for (i <- 0 until area.breite; j <- 0 until area.laenge) {
        
      }
      this
    }
  }

  def startNewRound(state: cState): cState = {
    val actualPlayer = nextPlayer(state.actualPlayer, state.player)
    val round = state.round + 1

    publish(NewRound(round))

    cState(state.dif, state.player, state.zombies, state.playerCount, actualPlayer, state.area, round, state.winCount)
  }

  def nextPlayer(actualPlayer: PlayerInterface, player: Array[PlayerInterface]): PlayerInterface = {
    val index = player.indexOf(actualPlayer)
    if (index < player.length - 1) {
      player(index + 1)
    } else {
      player(0)
    }
  }

  def zombieMove(zombie: ZombieInterface, x: Int, y: Int): ZombieInterface = {
    Zombie(zombie.area, zombie.name, zombie.strength, zombie.range, zombie.lifePoints);
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
