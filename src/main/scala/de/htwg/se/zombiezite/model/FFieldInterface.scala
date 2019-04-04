package de.htwg.se.zombiezite.model

trait FFieldInterface {
  val p: PositionInterface
  val chars: Vector[FCharacterInterface]
  val zombies: Vector[FZombieInterface]
  val players: Vector[FPlayerInterface]
  val charCount: Int
  val noiseCounter: Int

  def enterField(c: FCharacterInterface): FFieldInterface = {
    c match {
      case interface: FPlayerInterface =>
        enterPlayer(c.asInstanceOf[FPlayerInterface])
      case interface: FZombieInterface =>
        enterZombie(c.asInstanceOf[FZombieInterface])
      case _ =>
        this
    }
  }

  def enterPlayer(p: FPlayerInterface): FFieldInterface

  def enterZombie(z: FZombieInterface): FFieldInterface

  def leaveField(c: FCharacterInterface): FFieldInterface = {
    c match {
      case interface: FPlayerInterface =>
        leavePlayer(c.asInstanceOf[FPlayerInterface])
      case interface: FZombieInterface =>
        leaveZombies(c.asInstanceOf[FZombieInterface])
      case _ =>
        this
    }
  }

  def leavePlayer(p: FPlayerInterface): FFieldInterface

  def leaveZombies(z: FZombieInterface): FFieldInterface
}
