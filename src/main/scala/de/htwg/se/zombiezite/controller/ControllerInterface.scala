package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model.{ Item, Character, FieldInterface, PlayerInterface, ZombieInterface, Deck, ArmorInterface, WeaponInterface, AreaInterface }
import scala.collection.mutable.ArrayBuffer

trait ControllerInterface {
  var actualPlayer: PlayerInterface = null
  var zombieList: Array[String] = null
  var itemList: ArrayBuffer[String] = null
  var area: AreaInterface = null
  var player: Array[PlayerInterface] = null
  var zombies: ArrayBuffer[ZombieInterface] = null
  var itemDeck: Deck[Item] = null
  var zombieDeck: Deck[Array[ZombieInterface]] = null
  val playerNamer: Array[String] = Array("F. Maiar", "K. Kawaguchi", "H. Kaiba", "P. B. Rainbow")
  var fieldlength = 0
  var zombieCount = 0
  var playerCount = 0
  var zombiesKilled = 0
  var winCount = 50
  var round = 1
  
  def checkOrder
  def setDifficulty(dif: Int)
  def waitInput()
  def init(playerCounter: Int)
  def newRound
  def wait(p: PlayerInterface)
  def roundReset()
  def drawItem(): Item
  def attackableFields(char: Character): Array[FieldInterface]
  def availableWeapon(p: PlayerInterface): Array[Item]
  def drawZombie(): Array[ZombieInterface]
  def fullZombieTurn
  def zombieTurn(z: ZombieInterface)
  def move(char: Character, x: Int, y: Int)
  def search(p: PlayerInterface)
  def drop(pl: PlayerInterface, item: Item)
  def equipArmor(char: PlayerInterface, i: ArmorInterface)
  def beweapon(char: PlayerInterface, item: WeaponInterface)
  def attackZombie(pl: PlayerInterface, z: ZombieInterface)
  def attackPlayer(pl: PlayerInterface, z: ZombieInterface)
  def attackPlayerPlayer(atk: PlayerInterface, opf: PlayerInterface)
  def attackField(p: PlayerInterface, f: FieldInterface)
  def attackWholeField(p: PlayerInterface, f: FieldInterface): Boolean
  def getZombieList: Array[String]
  def getItemList: Array[String]
}