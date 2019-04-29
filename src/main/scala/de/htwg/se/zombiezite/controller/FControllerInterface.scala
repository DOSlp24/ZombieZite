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

  def init(): cState
  def wait(state: cState): cState
  def move(state: cState, direction: String): cState
  def search(state: cState): cState
  def drop(state: cState, item: FItemInterface): cState
  def equipArmor(state: cState, i: FArmorInterface): cState
  def beweapon(state: cState, weapon: FWeaponInterface): cState
  def triggerAttack(state: cState): cState
  def attackField(state: cState, f: FieldInterface): cState
  def stateToHtml(state: cState): String
}
