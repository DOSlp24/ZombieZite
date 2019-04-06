package de.htwg.se.zombiezite.model

trait FZombieInterface extends FCharacterInterface {
  override def walk(x: Int, y: Int): FZombieInterface
  override def takeDmg(dmg: Int): FZombieInterface
}
