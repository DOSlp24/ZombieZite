package de.htwg.se.zombiezite.model

case class ZombieDeck(area: Area) extends Deck[Array[Zombie]] {
  def draw(): Array[Zombie] = {
    val random: Integer = util.Random.nextInt(5)
    var zombies = Array.ofDim[Zombie](random)
    for (i <- 0 to random - 1) {
      zombies(i) = getDrawnZombie()
    }
    return zombies
  }

  def getDrawnZombie(): Zombie = {
    val randomZombie = util.Random.nextInt(5)
    val zombie: Zombie = randomZombie match {
      case 0 => new Zombie(area, "Schlurfer", 20, 1, 100)
      case 1 => new Zombie(area, "Runner", 10, 1, 70)
      case 2 => new Zombie(area, "Fatti", 40, 1, 250)
      case 3 => new Zombie(area, "Tank", 50, 2, 500)
      case 4 => new Zombie(area, "Spitter", 10, 3, 70)
    }
    zombie.actualField = area.line(area.laenge - 1)(area.breite / 2)
    zombie.actualField.zombies.append(zombie)
    zombie.actualField.chars.append(zombie)
    return zombie
  }
}