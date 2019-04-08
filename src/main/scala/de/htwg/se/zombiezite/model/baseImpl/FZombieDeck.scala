package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{ FDeckInterface, FZombieInterface }

case class FZombieDeck() extends FDeckInterface {
  final val MAX_ZOMBIE_SPAWN: Int = 5
  final val zombieNames: Vector[String] = Vector("Schlurfer", "Runner", "Fatti", "Tank", "Spitter")
  final val UNIQUE_ZOMBIE_COUNT: Int = zombieNames.length

  def draw(): Vector[FZombieInterface] = {
    val random: Integer = util.Random.nextInt(MAX_ZOMBIE_SPAWN)
    val z = (0 to random).toVector.map(_ => getDrawnZombie(util.Random.nextInt(UNIQUE_ZOMBIE_COUNT)))
    z
  }

  def getDrawnZombie(randomZombie: Int): FZombieInterface = {
    val zombie = randomZombie match {
      // TODO: Zombie spawn
      // Zombies  spawn at 2-2 right now, this might change soon.
      case 0 => FZombie(name = zombieNames(0), lifePoints = 150, strength = 19, x = 2, y = 2)
      case 1 => FZombie(name = zombieNames(1), lifePoints = 100, strength = 9, x = 2, y = 2)
      case 2 => FZombie(name = zombieNames(2), lifePoints = 250, strength = 39, range = 1, x = 2, y = 2)
      case 3 => FZombie(name = zombieNames(3), lifePoints = 500, strength = 49, range = 2, x = 2, y = 2)
      case 4 => FZombie(name = zombieNames(4), lifePoints = 70, strength = 9, range = 3, x = 2, y = 2)
    }

    zombie
  }
}