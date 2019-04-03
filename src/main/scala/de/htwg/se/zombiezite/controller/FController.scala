package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model
import de.htwg.se.zombiezite.model.baseImpl.{ Area, Zombie }
import de.htwg.se.zombiezite.model.{ PlayerInterface, ZombieInterface, _ }

import scala.swing.Publisher
import scala.swing.event.Event

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

case class DiscardWeapon(w: WeaponInterface) extends Event

case class CantDiscardFists() extends Event

case class CantDiscardFullInv() extends Event

case class ItemDropped(i: Item) extends Event

case class Consumed(i: ArmorInterface) extends Event

case class SwappedWeapon() extends Event

case class EquipedWeapon(w: WeaponInterface) extends Event

case class ArmorDamaged(name: String, typ: String, dmg: Int) extends Event

case class ArmorDestroyed(name: String, typ: String) extends Event

case class PlayerAttack(name: String, typ: String, dmg: Int) extends Event

case class PlayerAttackPlayer(atk: String, opf: String, dmg: Int) extends Event

case class ZombieDraw(z: Array[ZombieInterface]) extends Event

case class Wait(p: PlayerInterface) extends Event

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

case class NewAction() extends Event

case class StartZombieTurn() extends Event

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
