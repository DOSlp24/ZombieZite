package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model._

case class FField(
                   override val p: PositionInterface,
                   override val chars: Vector[FCharacterInterface],
                   override val zombies: Vector[FZombieInterface],
                   override val players: Vector[FPlayerInterface],
                   override val charCount: Int,
                   override val noiseCounter: Int
                 ) extends FFieldInterface {

  override def enterPlayer(p: FPlayerInterface): FFieldInterface = {
    val newChars: Vector[FCharacterInterface] = chars.+:(p)
    val newPlayers: Vector[FPlayerInterface] = players.+:(p)
    copy(chars = newChars, players = newPlayers, charCount = newChars.length)
  }

  override def enterZombie(z: FZombieInterface): FFieldInterface = {
    val newChars: Vector[FCharacterInterface] = chars.+:(z)
    val newZombies: Vector[FZombieInterface] = zombies.+:(z)
    copy(chars = newChars, zombies = newZombies, charCount = newChars.length)
  }
}
