package de.htwg.se.zombiezite.controller

import de.htwg.se.zombiezite.CustomTypes.ItemMonad
import de.htwg.se.zombiezite.model
import de.htwg.se.zombiezite.model.baseImpl._
import de.htwg.se.zombiezite.model.{PlayerInterface, ZombieInterface, _}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, Future}
import scala.swing.Publisher
import scala.swing.event.Event
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import slick.driver.H2Driver.api._

case class Update(state: cState) extends Event

//noinspection ScalaStyle
case class cState(
                   dif: Int = 2,
                   player: Vector[FPlayerInterface] = Vector[FPlayerInterface](),
                   zombies: Vector[FZombieInterface] = Vector[FZombieInterface](),
                   playerCount: Int = 0,
                   actualPlayer: Int = 0,
                   area: FAreaInterface = FArea(10, 10).build(),
                   round: Int = 0,
                   winCount: Int = 60,
                   zombiesKilled: Int = 0,
                   zombieDeck: FDeckInterface = FZombieDeck(),
                   itemDeck: FDeckInterface = FItemDeck().shuffle(),
                   playerOrder: Vector[String] = Vector[String]("F. Maiar", "K. Kawaguchi", "H. Kaiba", "P. B. Rainbow"),
                   won: Boolean = false,
                   lost: Boolean = false
                 ) {

  def updateChars(): cState = {
    val players = searchLinesForPlayers().sortWith((p1, p2) => playerOrder.indexOf(p1.name) < playerOrder.indexOf(p2.name))
    copy(zombies = searchLinesForZombies(), player = searchLinesForPlayers().sortWith((p1, p2) => playerOrder.indexOf(p1.name) < playerOrder.indexOf(p2.name))).checkActionCounter()
  }

  def updateAreaOverChar(): cState = {
    buildArea().enterFieldMulti(zombies ++ player)
  }

  def pushActualPlayer(newActualPlayer: FPlayerInterface): cState = {
    copy(player = player.updated(actualPlayer, newActualPlayer)).checkActionCounter()
  }

  def checkActionCounter(): cState = {
    if (player.length > actualPlayer) {
      val actionCounter = player(actualPlayer).actionCounter
      actionCounter match {
        case a if a > 0 => this
        case _ => nextPlayer()
      }
    } else {
      this
    }
  }

  def checkForDeadChars(): cState = {
    val newZombies = zombies.filter(z => z.lifePoints > 0)
    val newPlayer = player.filter(p => p.lifePoints > 0)
    copy(zombies = newZombies, player = newPlayer)
  }

  def checkForGameOver(): cState = {
    if (player.isEmpty) {
      copy(lost = true)
    }
    if (round == 30) {
      copy(won = true)
    }
    this
  }

  def searchLinesForZombies(line: Int = 0): Vector[FZombieInterface] = {
    if (line < area.len) {
      searchLinesForZombies(line + 1) ++ searchFieldForZombies(line, 0)
    } else {
      Vector[FZombieInterface]()
    }
  }

  def searchFieldForZombies(line: Int, field: Int): Vector[FZombieInterface] = {
    if (field < area.wid) {
      searchFieldForZombies(line, field + 1) ++ area.lines(line)(field).zombies.filter(z => z.lifePoints > 0)
    } else {
      Vector[FZombieInterface]()
    }
  }

  def searchLinesForPlayers(line: Int = 0): Vector[FPlayerInterface] = {
    area.lines.flatten.flatMap(f => f.players)
  }

  def buildArea(): cState = {
    copy(area = FArea(area.len, area.wid).build())
  }

  def enterFieldMulti(chars: Vector[FCharacterInterface]): cState = {
    if (chars.nonEmpty) {
      enterField(chars.head).enterFieldMulti(chars.drop(1))
    } else {
      this
    }
  }

  def enterField(c: FCharacterInterface): cState = {
    c match {
      case interface: FPlayerInterface => enterFieldPlayer(interface)
      case interface: FZombieInterface => enterFieldZombie(interface)
    }
  }

  def enterFieldPlayer(p: FPlayerInterface): cState = {
    player.length match {
      case 0 => copy(actualPlayer = 0, area = area.putField(area.lines(p.y)(p.x).enterField(p))).updateChars()
      case _ => copy(area = area.putField(area.lines(p.y)(p.x).enterField(p))).updateChars()
    }
  }

  def enterFieldZombie(z: FZombieInterface): cState = {
    copy(area = area.putField(area.lines(z.y)(z.x).enterField(z))).updateChars()
  }

  def leaveField(c: FCharacterInterface): cState = {
    copy(area = area.putField(area.lines(c.y)(c.x).leaveField(c)))
  }

  def moveUp(c: FCharacterInterface): cState = {
    player(actualPlayer).y match {
      case y if y > 0 => leaveField(c).enterField(c.walk(0, -1))
      case _ => this
    }
  }

  def moveDown(c: FCharacterInterface): cState = {
    player(actualPlayer).y match {
      case y if y < area.len - 1 => leaveField(c).enterField(c.walk(0, 1))
      case _ => this
    }
  }

  def moveLeft(c: FCharacterInterface): cState = {
    player(actualPlayer).x match {
      case x if x > 0 => leaveField(c).enterField(c.walk(-1, 0))
      case _ => this
    }
  }

  def moveRight(c: FCharacterInterface): cState = {
    player(actualPlayer).x match {
      case x if x < area.wid - 1 => leaveField(c).enterField(c.walk(1, 0))
      case _ => this
    }
  }

  def pWait(): cState = {
    nextPlayer()
  }

  def drawItem(): cState = {
    val drawnItems: ItemMonad[Option[FItemInterface]] = itemDeck.asInstanceOf[FItemDeck].draw()
    /*val drawnItems: ItemMonad[Future[FItemInterface]] = itemDeck.asInstanceOf[FItemDeck].draw()

    val drawnItem = for (item <- drawnItems.items) yield Await.result(item, 10 seconds) match {
      case i => i
    }*/
    val drawnItem = {
      for (item <- drawnItems.items) yield item match {
        case Some(i) => i
        case None => FTrash("Trash")
      }
    }
    val newActualPlayer = player(actualPlayer).takeItem(drawnItem.head)
    copy(itemDeck = itemDeck.asInstanceOf[FItemDeck].afterDraw(), player = player.updated(actualPlayer, newActualPlayer)).checkActionCounter()
  }

  def dropItem(i: FItemInterface): cState = {
    val newActualPlayer = player(actualPlayer).drop(i)
    pushActualPlayer(newActualPlayer)
  }

  def equipWeapon(w: FWeaponInterface): cState = {
    val newActualPlayer = player(actualPlayer).equipWeapon(w)
    pushActualPlayer(newActualPlayer)
  }

  def useArmor(a: FArmorInterface): cState = {
    val newActualPlayer = player(actualPlayer).useArmor(a)
    pushActualPlayer(newActualPlayer)
  }

  def drawZombie(): cState = {
    val zombiesDrawn = zombieDeck.asInstanceOf[FZombieDeck].draw()
    enterFieldMulti(chars = zombiesDrawn).updateChars()
  }

  def increaseRoundCount(): cState = {
    copy(round = round + 1)
  }

  def nextPlayer(): cState = {
    val actual = player(actualPlayer)
    if (actual.name != playerOrder.last && this.actualPlayer < player.length - 1) {
      copy(actualPlayer = actualPlayer + 1, player = player.updated(actualPlayer + 1, player(actualPlayer + 1).resetActionCounter))
    } else {
      copy(actualPlayer = 0, player = player.updated(0, player(0).resetActionCounter)).startNewTurn()
    }
  }

  def startNewTurn(): cState = {
    //increaseRoundCount()
    increaseRoundCount().zombieTurn().drawZombie()
  }

  def zombieTurn(): cState = {
    val newZombies: Vector[FZombieInterface] = zombies.map {
      case z if z.name == "Runner" => executeZombieTurn(executeZombieTurn(z)) // Runner trigger twice
      case z => executeZombieTurn(z)
    }
    val ret = copy(zombies = newZombies)
    ret.zombies.length match {
      case 0 => ret.updateAreaOverChar()
      case _ => ret.zombiesTriggerAttack().updateAreaOverChar()
    }
  }

  def executeZombieTurn(z: FZombieInterface): FZombieInterface = {
    val canSeePlayer = player.filter(p => {
      p.x == z.x || p.y == z.y
    })

    if (canSeePlayer.nonEmpty) {

      val canAttackPlayer = canSeePlayer.filter(p => {
        math.abs(z.y - p.y) + math.abs(z.x - p.x) <= z.range
      })

      if (canAttackPlayer.nonEmpty) {
        zombieAttack(z, canAttackPlayer.apply(0))
      } else {
        zombieMoveTowards(canSeePlayer.apply(0), z)
      }
    } else {
      zombieMove(z)
    }
  }

  def zombieMove(z: FZombieInterface): FZombieInterface = {
    z match {
      case _ if z.x == area.wid - 1 => z.walk(-1, 0)
      case _ if z.x == 0 => z.walk(1, 0)
      case _ if z.y == area.len - 1 => z.walk(0, -1)
      case _ if z.y == 0 => z.walk(0, 1)
      case _ => {
        val random = scala.util.Random.nextInt(4)
        random match {
          case 0 => z.walk(0, 1)
          case 1 => z.walk(0, -1)
          case 2 => z.walk(1, 0)
          case 3 => z.walk(-1, 0)
        }
      }
    }
  }

  def zombieMoveTowards(p: FPlayerInterface, z: FZombieInterface): FZombieInterface = {
    //Move towards player

    val x = p.x - z.x
    val y = p.y - z.y

    if (x < 0) {
      z.walk(-1, 0)
    } else if (x > 0) {
      z.walk(1, 0)
    } else {
      if (y < 0) {
        z.walk(0, -1)
      } else if (y > 0) {
        z.walk(0, 1)
      } else {
        z
      }
    }
  }

  def zombieAttack(z: FZombieInterface, p: FPlayerInterface): FZombieInterface = {
    z.selectTarget(p)
    //p.takeDmg(z.equippedWeapon.strength * z.strength)
  }

  def zombiesTriggerAttack(): cState = {
    val kawaDmg = zombies.filter(zombie => zombie.archenemy.name.contains("Kawaguchi")).map(z => z.strength).sum
    val maiarDmg = zombies.filter(zombie => zombie.archenemy.name.contains("Maiar")).map(z => z.strength).sum
    val rainDmg = zombies.filter(zombie => zombie.archenemy.name.contains("Rainbow")).map(z => z.strength).sum
    val kaibaDmg = zombies.filter(zombie => zombie.archenemy.name.contains("Kaiba")).map(z => z.strength).sum
    val newKawa = player.filter(p => p.name.contains("Kawaguchi")).map(p => p.takeDmg(kawaDmg))
    val newMaiar = player.filter(p => p.name.contains("Maiar")).map(p => p.takeDmg(maiarDmg))
    val newRain = player.filter(p => p.name.contains("Rainbow")).map(p => p.takeDmg(rainDmg))
    val newKaiba = player.filter(p => p.name.contains("Kaiba")).map(p => p.takeDmg(kaibaDmg))
    copy(zombies = zombies.map(z => z.selectTarget(FPlayerWithoutIdentity())), player = Vector() ++ newMaiar ++ newKawa ++ newKaiba ++ newRain).checkForDeadChars().updateAreaOverChar().checkForGameOver()
  }

  override def toString: String = {
    /*val lineseparator = ("+-" + ("--" * blocknum)) * blocknum + "+\n"
    val line = ("| " + ("x " * blocknum)) * blocknum + "|\n"
    var box = "\n" + (lineseparator + (line * blocknum)) * blocknum + lineseparator
    for {
      row <- 0 until size
      col <- 0 until size
    } box = box.replaceFirst("x ", cell(row, col).toString)
    */
    //"box"
    "actualPlayer: " + player(actualPlayer).name
  }

  def toHtml(): String = {
    "<p  style=\"font-family:'Lucida Console', monospace\"> " +
      toString.replace("\n", "<br>").replace("  ", " _") + "</p>"
  }
}

//noinspection ScalaStyle
class FController() extends Publisher with FControllerInterface {

  override def init(): cState = {
    val p1 = FPlayer(name = "F. Maiar", x = 5, y = 0)
    val p2 = FPlayer(name = "K. Kawaguchi", x = 5, y = 0)
    val p3 = FPlayer(name = "H. Kaiba", x = 5, y = 0)
    val p4 = FPlayer(name = "P. B. Rainbow", x = 5, y = 0)
    val retState = cState().buildArea().enterField(p1).enterField(p2).enterField(p3).enterField(p4)
    publish(Update(retState))
    retState
  }

  def startNewRound(state: cState): cState = {
    val newPlayer = nextPlayer(state)
    val round = state.round + 1

    val retState = cState(actualPlayer = newPlayer, round = round)
    publish(Update(retState))
    retState
  }

  def nextPlayer(state: cState): Int = {
    val actualPlayer = state.actualPlayer
    val lastPlayer = state.player.length - 1
    actualPlayer match {
      case _ if actualPlayer == lastPlayer => 0
      case _ => actualPlayer + 1
    }
  }

  override def wait(state: cState): cState = {
    val retState = state.pWait
    publish(Update(retState))
    retState
  }

  override def move(state: cState, direction: String): cState = {
    val retState: cState = direction match {
      case "up" => state.moveUp(state.player(state.actualPlayer))
      case "down" => state.moveDown(state.player(state.actualPlayer))
      case "left" => state.moveLeft(state.player(state.actualPlayer))
      case "right" => state.moveRight(state.player(state.actualPlayer))
    }
    publish(Update(retState))
    retState
  }

  override def search(state: cState): cState = {
    val retState = state.drawItem()
    publish(Update(retState))
    retState
  }

  override def drop(state: cState, item: FItemInterface): cState = {
    val retState = state.dropItem(item)
    publish(Update(retState))
    retState
  }

  def dropBySlot(state: cState, slot: Integer): cState = {
    drop(state, state.player(state.actualPlayer).equipment(slot))
  }

  override def equipArmor(state: cState, i: FArmorInterface): cState = {
    val retState = state.useArmor(i)
    publish(Update(retState))
    retState
  }

  override def beweapon(state: cState, weapon: FWeaponInterface): cState = {
    val retState = state.equipWeapon(weapon)
    publish(Update(retState))
    retState
  }

  def equipBySlot(state: cState, slot: Integer): cState = {
    state.player(state.actualPlayer).equipment(slot) match {
      case weapon: FWeaponInterface => beweapon(state, weapon)
      case armor: FArmorInterface => equipArmor(state, armor)
    }
  }

  override def triggerAttack(state: cState): cState = {
    state
  }

  override def attackField(state: cState, f: FieldInterface): cState = {
    state
  }

  override def stateToHtml(state: cState): String = {
    state.toHtml()
  }

  def slickstuff(): Unit = {
    val db = Database.forConfig("zombieDb")
    try {
      class Area(tag: Tag) extends Table[(Int, Int, Int)](tag, "Area") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def len = column[Int]("Length")

        def wid = column[Int]("Width")

        def * = (id, len, wid)
      }

      val areaTable = TableQuery[Area]

      class Field(tag: Tag) extends Table[(Int, Int, Int, Int)](tag, "Field") {
        //def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def x = column[Int]("X", O.PrimaryKey)

        def y = column[Int]("Y", O.PrimaryKey)

        def areaID = column[Int]("F_Area")

        def charCount = column[Int]("CharCount")

        def * = (x, y, areaID, charCount)

        def area = foreignKey("MY_AREA", areaID, areaTable)(_.id)
      }

      val fieldTable = TableQuery[Field]

      class Player(tag: Tag) extends Table[(Int, Int, Int, String, Int, Int, Int, Int)](tag, "Player") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def fieldX = column[Int]("F_FieldX")

        def fieldY = column[Int]("F_FieldY")

        def name = column[String]("Name")

        def lifepoints = column[Int]("Lifepoints")

        def armor = column[Int]("Armor")

        def stren = column[Int]("Strength")

        def ran = column[Int]("Range")

        def * = (id, fieldX, fieldY, name, lifepoints, armor, stren, ran)

        def myFieldX = foreignKey("MY_FIELDX", fieldX, fieldTable)

        def myFieldY = foreignKey("MY_FIELDY", fieldY, fieldTable)
      }

      val playerTable = TableQuery[Player]

      class Zombie(tag: Tag) extends Table[(Int, Int, Int, String)](tag, "Zombie") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def fieldX = column[Int]("F_FieldX")

        def fieldY = column[Int]("F_FieldY")

        def name = column[String]("Name")

        def * = (id, fieldX, fieldY, name)

        def myFieldX = foreignKey("MY_FIELDX", fieldX, fieldTable)

        def myFieldY = foreignKey("MY_FIELDY", fieldY, fieldTable)
      }

      val zombieTable = TableQuery[Zombie]

      class Weapon(tag: Tag) extends Table[(Int, Int, Boolean, String, Int, Int, Int)](tag, "Weapon") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def belongsTo = column[Int]("Owner")

        def isEquipped = column[Boolean]("IsEquipped")

        def name = column[String]("Name")

        def str = column[Int]("Strength")

        def ran = column[Int]("Range")

        def aoe = column[Int]("AOE")

        def * = (id, belongsTo, isEquipped, name, str, ran, aoe)

        def owner = foreignKey("MY_OWNER", belongsTo, playerTable)(_.id)
      }

      val weaponTable = TableQuery[Weapon]

      class Armor(tag: Tag) extends Table[(Int, Int, String, Int)](tag, "Armor") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def ownerID = column[Int]("Owner")

        def name = column[String]("Name")

        def protection = column[Int]("Protection")

        def * = (id, ownerID, name, protection)

        def owner = foreignKey("MY_OWNER", ownerID, playerTable)(_.id)
      }

      val armorTable = TableQuery[Armor]

      class Trash(tag: Tag) extends Table[(Int, Int, String)](tag, "Trash") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

        def ownerID = column[Int]("Owner")

        def name = column[String]("Name")

        def * = (id, ownerID, name)

        def owner = foreignKey("MY_OWNER", ownerID, playerTable)(_.id)
      }

      val trashTable = TableQuery[Trash]
    } finally db.close
  }
}
