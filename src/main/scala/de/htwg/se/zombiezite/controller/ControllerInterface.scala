package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.model.{ Item, Character, FieldInterface, PlayerInterface, ZombieInterface, Deck, ArmorInterface, WeaponInterface }

trait ControllerInterface {
  def checkOrder
  def setDifficulty(dif: Int)
  def waitInput()
  def init(playerCounter: Int)
  def newRound
  def wait(p: PlayerInterface)
  def roundReset()
  def drawItem(): Item
  def attackableFields(char: Character): Array[FieldInterface]
  def availableWeapon(p: PlayerInterface): Array[Int]
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
}