package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model.{ AreaInterface, ArmorInterface, Character, Deck, FArmorInterface, FItemInterface, FPlayerInterface, FWeaponInterface, FZombieInterface, FieldInterface, Item, PlayerInterface, WeaponInterface, ZombieInterface }

import scala.collection.mutable.ArrayBuffer

trait FControllerInterface {
  /*val actualPlayer: PlayerInterface
  val zombieList: Array[String]
  val itemList: ArrayBuffer[String]
  val area: AreaInterface
  val player: Vector[FPlayerInterface]
  val zombies: Vector[FZombieInterface]
  val itemDeck: Deck[Item]
  val zombieDeck: Deck[Array[ZombieInterface]]
  val playerNamer: Array[String] = Array("F. Maiar", "K. Kawaguchi", "H. Kaiba", "P. B. Rainbow")
  val fieldlength: Int
  val zombieCount: Int
  val playerCount: Int
  val zombiesKilled: Int
  val winCount: Int
  val round: Int*/

  def checkOrder
  def setDifficulty(dif: Int)
  def waitInput()
  def init(playerCounter: Int)
  def newRound
  def wait(state: cState): cState
  def roundReset()
  def drawItem(): Item
  def attackableFields(char: Character): Array[FieldInterface]
  def availableWeapon(p: PlayerInterface): Array[Item]
  def drawZombie(): Array[ZombieInterface]
  def fullZombieTurn
  def zombieTurn(z: ZombieInterface)
  def move(state: cState, direction: String): cState
  def search(state: cState): cState
  def drop(state: cState, item: FItemInterface): cState
  def equipArmor(state: cState, i: FArmorInterface): cState
  def beweapon(state: cState, weapon: FWeaponInterface): cState
  def attackZombie(pl: PlayerInterface, z: ZombieInterface)
  def attackPlayer(pl: PlayerInterface, z: ZombieInterface)
  def attackPlayerPlayer(atk: PlayerInterface, opf: PlayerInterface)
  def attackField(p: PlayerInterface, f: FieldInterface)
  def attackWholeField(p: PlayerInterface, f: FieldInterface): Boolean
  def getZombieList: Array[String]
  def getItemList: Array[String]
}
