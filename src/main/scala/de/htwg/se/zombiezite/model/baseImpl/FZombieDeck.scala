package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{ FDeckInterface, FZombieInterface }

case class FZombieDeck(area: Area) extends FDeckInterface {
  final val MAX_ZOMBIE_SPAWN: Int = 5
  val zombieNames: Array[String] = Array("Schlurfer", "Runner", "Fatti", "Tank", "Spitter")
  final val UNIQUE_ZOMBIE_COUNT: Int = zombieNames.length

  def draw(): Array[FZombieInterface] = {
    val random: Integer = util.Random.nextInt(MAX_ZOMBIE_SPAWN)
    val zombies = Array.ofDim[FZombieInterface](random)
    for (i <- 0 until random - 1) {
      val randomZombie = util.Random.nextInt(UNIQUE_ZOMBIE_COUNT)
      zombies(i) = getDrawnZombie(randomZombie)
    }
    zombies
  }

  def getDrawnZombie(randomZombie: Int): FZombieInterface = {
    val zombie = randomZombie match {
      // TODO: actualField
      // actualField and area are null because i did not know better...
      // TODO: Zombie spawn
      // Zombies  spawn at 2-2 right now, this might change soon.
      case 0 => FZombie(name = zombieNames(0), lifePoints = 150, strength = 19, x = 2, y = 2, actualField = null, area = null)
      case 1 => FZombie(name = zombieNames(1), lifePoints = 100, strength = 9, x = 2, y = 2, actualField = null, area = null, actionCounter = 2)
      case 2 => FZombie(name = zombieNames(2), lifePoints = 250, strength = 39, range = 1, x = 2, y = 2, actualField = null, area = null)
      case 3 => FZombie(name = zombieNames(3), lifePoints = 500, strength = 49, range = 2, x = 2, y = 2, actualField = null, area = null)
      case 4 => FZombie(name = zombieNames(4), lifePoints = 70, strength = 9, range = 3, x = 2, y = 2, actualField = null, area = null)
    }

    zombie
  }
}