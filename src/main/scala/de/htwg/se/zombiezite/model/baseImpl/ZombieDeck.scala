package de.htwg.se.zombiezite.model.baseImpl

import de.htwg.se.zombiezite.model.{Character, Deck, ZombieInterface}

case class ZombieDeck(area: Area) extends Deck[Array[ZombieInterface]] {
  val zombieList: Array[String] = Array("Schlurfer", "Runner", "Fatti", "Tank", "Spitter")
  def draw(): Array[ZombieInterface] = {
    val random: Integer = util.Random.nextInt(5)
    var zombies = Array.ofDim[ZombieInterface](random)
    for (i <- 0 to random - 1) {
      zombies(i) = getDrawnZombie()
    }
    return zombies
  }

  def getDrawnZombie(): ZombieInterface = {
    val randomZombie = util.Random.nextInt(5)
    return getDrawnZombieR(randomZombie)
  }

  def getDrawnZombieR(randomZombie: Int): ZombieInterface = {
    val zombie: Zombie = randomZombie match {
      case 0 => new Zombie(area, zombieList(0), 19, 0, 100)
      case 1 => new Zombie(area, zombieList(1), 9, 0, 70)
      case 2 => new Zombie(area, zombieList(2), 39, 0, 250)
      case 3 => new Zombie(area, zombieList(3), 49, 1, 500)
      case 4 => new Zombie(area, zombieList(4), 9, 3, 70)
      case _ => return null
    }
    zombie.actualField = area.line(area.laenge - 1)(area.breite / 2)
    zombie.actualField.zombies.append(zombie)
    zombie.actualField.chars.append(zombie)
    return zombie
  }
}