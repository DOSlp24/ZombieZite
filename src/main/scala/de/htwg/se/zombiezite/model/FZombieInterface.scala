package de.htwg.se.zombiezite.model

trait FZombieInterface extends FCharacterInterface {
  val archenemy: FPlayerInterface

  override def walk(x: Int, y: Int): FZombieInterface

  override def takeDmg(dmg: Int): FZombieInterface

  def selectTarget(c: FPlayerInterface): FZombieInterface
}
