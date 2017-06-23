package de.htwg.se.zombiezite.aview

import de.htwg.se.zombiezite.util.Observer
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller._
import scala.io.StdIn
import swing._
import com.sun.org.apache.xalan.internal.xsltc.compiler.ForEach

class Tui(controller: Controller) extends Reactor {
  var status = "roundStart"

  listenTo(controller)

  reactions += {
    case e: GameOverLost => lost
    case e: GameOverWon => won
    case e: Fail => {
      println("Angriff fehlgeschlagen!")
    }
    case e: ZombieWentUp => {
      println("Ein " + e.typ + " ist ein Feld nach oben gelaufen. Aktuelles Feld: (" + e.x + ", " + e.y + ")")
    }
    case e: ZombieWentDown => {
      println("Ein " + e.typ + " ist ein Feld runter gelaufen. Aktuelles Feld: (" + e.x + ", " + e.y + ")")
    }
    case e: ZombieWentRight => {
      println("Ein " + e.typ + " ist ein Feld nach rechts gelaufen. Aktuelles Feld: (" + e.x + ", " + e.y + ")")
    }
    case e: ZombieWentLeft => {
      println("Ein " + e.typ + " ist ein Feld nach links gelaufen. Aktuelles Feld: (" + e.x + ", " + e.y + ")")
    }
    case e: ZombieAttack => {
      println("Ein " + e.typ + " fügte " + e.name + " [" + e.dmg + "] Schaden zu!")
    }
    case e: DeadPlayer => {
      println("AAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHH!!!!\nIch sterbe!!!\n\n")
      println(e.name + " wurde von " + e.murderer + " brutal zerrissen und getötet!")
    }
    case e: DeadZombie => {
      println(e.name + " erledigte einen " + e.typ)
      println("Ein " + e.typ + " weniger")
    }
    case e: DiscardWeapon => {
      println("Du hast {" + e.w.name + "|" + e.w.range + " R|" + e.w.strength + " S" + "} abgelegt.\nDu kämpfst ab jetzt mit bloßen Fäusten!")
    }
    case e: CantDiscardFists => {
      println("Du kannst deine Fäuste nicht ablegen - Du bist kein Zombie!")
    }
    case e: CantDiscardFullInv => {
      println("Dein Inventar ist voll!\nIn diesen Rucksack bekommst du deine Waffe nicht mehr rein.")
    }
    case e: SwappedWeapon => {
      println("Waffen erfolgreich gewechselt!")
    }
    case e: EquipedWeapon => {
      println("Du hast {" + e.w.name + "|" + e.w.range + " R|" + e.w.strength + " S" + "} ausgerüstet.\nHervorragende Wahl!")
    }
    case e: ArmorDamaged => {
      println(e.name + "'s Rüstung wurde von einem " + e.typ + " um <" + e.dmg + "> Punkte reduziert!")
    }
    case e: ArmorDestroyed => {
      println(e.name + "'s Rüstung wurde von einem " + e.typ + " komplett zerfetzt!")
    }
    case e: PlayerAttack => {
      println(e.name + " traf einen " + e.typ + " für [" + e.dmg + "] Schaden!")
    }
    case e: PlayerAttackPlayer => {
      println(e.atk + " fügte seinem VERBÜNDETEN " + e.opf + " [" + e.dmg + "] Schaden zu!")
    }
    case e: PlayerMove => {
      println(controller.actualPlayer.name + " Ist gelaufen. Neues Feld (" + e.x + ", " + e.y + ")")
      status = "roundStart"
    }
    case e: Wait => {
      println(e.p.name + " setzt sich auf den Boden und wartet darauf was passiert.")
      status = "roundStart"
      printRoundStart
    }
    case e: NewRound => {
      gameStatus
      printRoundStart
      status = "roundStart"
    }
    case e: StartSpieler => {
      println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Aufbau::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
      println("Wieviele Spieler spielen mit? (2-4)")
      status = "playerCount"
    }
    case e: StartSchwierig => {
      println("Wie lange soll das Spiel gehen?\n[0] Kurz\n[1] Mittel\n[2] Lang")
      this.fieldlength = controller.fieldlength
      status = "dif"
    }
    case e: Start => {
      init
      printRoundStart
      status = "roundStart"
    }
    case e: Search => {
      println("\n********Suche********\n")
      search(e.i)
      printEq
    }
    case e: WaitInput => {
      if (controller.actualPlayer.actionCounter != 0) {
        printRoundStart
      }
      status = "roundStart"
    }
    case e: AktivierungZombies => {
      println("*********************Aktivierung Zombies abgeschlossen*********************\n\n")
    }
    case e: AktivierungRunner => {
      println("*********************Zusatzaktivierung Runner abgeschlossen*********************\n")
    }
    case e: NewAction => {
      if (controller.actualPlayer.actionCounter != 0) {
        printRoundStart
      }
      status = "roundStart"
    }
    case e: StartZombieTurn => {
      println("\n")
    }
    case e: ItemDropped => {
      println(controller.actualPlayer.name + " hat " + e.i.name + " fallen gelassen!")
    }
    case e: Consumed => {
      println(e.i.name + " wurde verbraucht!")
    }
  }

  def compute(act: String) {
    status match {
      case "roundStart" => {
        round(controller.actualPlayer, act)
      }
      case "playerCount" => {
        playerCount(act)
      }
      case "dif" => {
        difficulty(act)
      }
      case "equip" => {
        equip(act, controller.actualPlayer, controller.actualPlayer.equipment)
      }
      case "attack" => {
        attack(act, controller.actualPlayer)
      }
      case "move" => {
        move(act, controller.actualPlayer)
      }
      case "beweapon" => {
        beweapon(act, controller.actualPlayer, controller.actualPlayer.equipment)
      }
      case "manageWeapon" => {
        manageWeapon(act, controller.actualPlayer.equipment, controller.actualPlayer)
      }
      case "drop" => {
        drop(act, controller.actualPlayer)
      }
      case "armor" => {
        armor(act, controller.actualPlayer, controller.actualPlayer.equipment)
      }
    }
  }

  def printRoundStart {
    println("\n\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + controller.actualPlayer.name + " ist dran - Aktionen übrig [" + controller.actualPlayer.actionCounter + "]:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("Was willst du tun " + controller.actualPlayer.name + "? (b (Bewegung), s (Suche), a (Ausrüstung), an(Angriff), w(Warten))")
  }

  def won = {
    println("\n\n\n|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|")
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println(":_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:::::  SIEG  :::::-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`")
  }

  def lost = {
    println("\n\n\n|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|")
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println(":_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:::::Game Over:::::-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`")
  }

  def playerCount(playerCount: String) {
    playerCount match {
      case "2" | "3" | "4" => {
        println(playerCount + " Spieler starten.\nLos geht's!\n")
        controller.init(playerCount.toInt)
      }
      case _ => {
        println("Falsche Eingabe : *" + playerCount + "*!")
      }
    }
  }

  def difficulty(act: String) {
    act match {
      case "0" | "1" | "2" => {
        controller.setDifficulty(act.toInt + 1)
      }
      case _ => {
        println("Falsche Eingabe - Die Schwierigkeit wird auf *1* gesetzt.")
        controller.setDifficulty(1)
      }
    }
  }

  var fieldlength = 0

  def init() {
    println("::::::::::::::::::::Spielfeld wird aufgebaut::::::::::::::::::::")
    print("  ")
    for (i <- 0 to controller.area.breite - 1) {
      print(" ___ " + i)
    }
    for (i <- 0 to controller.area.laenge - 1) {
      println("|\n" + i)
    }
    println("\nAufbau abgeschlossen\n::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("\n:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-: Spielbegin :-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
  }

  def gameStatus {
    println("\n\n°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°")
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Runde " + controller.round + "::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°\n")
    println("                                                        Zombies getötet: [" + controller.zombiesKilled + "/" + controller.winCount + "]                                                        ")
    println("\n*******************************************************************************************")
    println("::::::::::::::::::::::::::::::::::::::::::::::::Positionen der Spieler [" + controller.playerCount + "]::::::::::::::::::::::::::::::::::::::::::::::::")
    playerPos()
    println("*******************************************************************************************")
    println(":::::::::::::::::::::::::::::::::::::::::::::::Positionen der Zombies [" + controller.zombieCount + "]:::::::::::::::::::::::::::::::::::::::::::::::")
    zombiePos()
    println("*******************************************************************************************\n\n")
  }

  def round(p: Player, act: String) {
    var eq = p.equipment
    act match {

      case "b" => {

        println("\n********Bewegung********\n")

        println("In welche Richtung willst du? *ho(hoch), ru(runter), li(links), re(rechts)*")
        status = "move"
      }

      case "s" =>
        controller.search(p)

        case"a" => {

          println("\n\n\n\n********Deine Ausrüstung********")

          printEq()
          if (!eq.isEmpty) {
            println("\n\nWas willst du damit tun? (w (Waffe wechseln), f (Item fallen lassen), a (Ausrüstung benutzen), x (nichts tun))")
            status = "equip"
          } else {
            println()
          }
        }

        case"an" => {
          println("\nDiese Felder kannst du angreifen:")
          var i = 0
          val af = controller.attackableFields(p)
          af.foreach { f =>
            println("[" + i + "] " + charsOnField(f))
            i += 1
          }
          println("\n\nIn welches Feld willst du angreifen? ([x] zum Abbrechen.)")
          status = "attack"
        }
        case"w" => {
          controller.wait(p)
        }
        case _ => println("Absolut falsche Eingabe!")
    }
  }

  def attack(act: String, p: Player) {
    var i = 0
    val af = controller.attackableFields(p)
    af.foreach { f =>
      println("[" + i + "] " + charsOnField(f))
      i += 1
    }
    if (act != "") {
      if (act.forall { x => x.isDigit }) {
        if (act.toInt < af.length) {
            controller.attackField(p, af(act.toInt))
          
        } else {
          println("Falsche Eingabe: " + act + " ist zu gross!")
          status = "roundStart"
          printRoundStart
        }
      } else if (act == "x") {
        println("Abgebrochen!\n")
        status = "roundStart"
        printRoundStart
      } else {
        println("Falsche Eingabe: " + act + " ist keine Zahl!")
        status = "roundStart"
        printRoundStart
      }
    } else {
      println("Falsche Eingabe - Nichts zählt nicht!")
      status = "roundStart"
      printRoundStart
    }
  }

  def charsOnField(f: Field): String = {
    if (!f.chars.isEmpty) {
      f.chars.foreach { c =>
        if (c.lifePoints == 0) {
          c.die()
        }
      }
      f.zombies.foreach { z =>
        if (z.lifePoints == 0) {
          z.die()
        }
      }
      f.players.foreach { p =>
        if (p.lifePoints == 0) {
          p.die()
        }
      }
    }
    var s: StringBuilder = new StringBuilder
    s.append("Feld (" + f.p.x / fieldlength + "," + f.p.y / fieldlength + ") Charaktere [")
    if (f.chars.isEmpty) {
      s.append(" Keine Charaktere auf diesem Feld ]")
      return s.toString()
    }
    if (f.players.isEmpty) {
      s.append(" Keine Spieler auf diesem Feld |")
    } else {
      f.players.foreach { p => s.append("| " + p.name + " |") }
      s.append(" -- ")
    }
    if (f.zombies.isEmpty) {
      s.append("| Keine Zombies auf diesem Feld |")
    } else {
      f.zombies.foreach { z => s.append("| " + z.name + " [" + z.lifePoints + " LP] | ") }
    }
    s.append("]")
    return s.toString()
  }

  def move(act: String, p: Player): Int = {
    act match {

      case "ho" => {
        controller.move(p, 0, 1)
      }

      case "ru" => {
        controller.move(p, 0, -1)
      }

      case "li" => {
        controller.move(p, -1, 0)
      }

      case "re" => {
        controller.move(p, 1, 0)
      }

      case _ => {
        println("Falsche Eingabe! Das ist keine Richtung.")
        status = "roundStart"
        printRoundStart
        return 0
      }
    }
    status = "roundStart"
    printRoundStart
    return 1
  }

  def search(i: Item): Int = {
    if (i != null) {
      println("\n\nDu suchst auf dem aktuellen Feld nach etwas Nützlichem.")
      println("Du hast " + i + " gefunden und in deinen Rucksack gepackt")
      println("\n\n***********Deine aktuelle Ausrüstung***********")
      println("******************************************")
      println()
      status = "roundStart"
      printRoundStart
      return 1
    } else {
      println("*********Du kannst nicht suchen. Dein Rucksack ist voll*********\n")
      status = "roundStart"
      printRoundStart
      return 0
    }
  }

  def equip(act: String, p: Player, eq: ArrayBuffer[Item]) {
    act match {

      case "w" => {

        println("\n********Waffen********\n")

        println("Willst du eine Waffe aus deinem Rucksack ausrüsten (au) oder deine aktuelle ablegen (ab)?")
        status = "manageWeapon"
      }

      case "f" => {

        println("\n********Drop********\n")
        println("Welches Item willst du fallen lassen?")
        status = "drop"
      }

      case "a" => {

        println("\n********Rüstung********\n")
        manageArmor(eq, p)
      }

      case "x" => {
        println("Selber Schuld.")
        status = "roundStart"
        printRoundStart
      }
      case _ => {
        println("Einfach nö!")
        status = "roundStart"
        printRoundStart
      }
    }
  }

  def drop(act: String, p: Player) {
    val eq = p.equipment
    if (canDrop(act, eq)) {
      controller.drop(p, eq(act.toInt))
    }
    status = "roundStart"
    printRoundStart
  }

  def canDrop(act: String, eq: ArrayBuffer[Item]): Boolean = {
    if (act != "") {
      act.forall { x => x.isDigit } match {
        case true => {
          val actInt = act.toInt
          if (actInt > eq.length - 1) {
            println("Du musst eine Zahl zwischen [0] und [" + (eq.length - 1) + "] eingeben")
            return false
          } else {
            println(eq(actInt).name + " wurde aus deinem Rucksack geworfen")
            return true
          }
        }
        case false => {
          println("Du musst eine Zahl eingeben.\n")
          return false
        }
      }
    } else {
      return false
    }
  }

  def manageArmor(equ: ArrayBuffer[Item], p: Player) {

    var armor = ArrayBuffer[Int]()
    for (ws <- 0 to equ.length - 1) {
      if (equ(ws).isInstanceOf[Armor]) {
        armor.append(ws)
      }
    }
    if (!armor.isEmpty) {
      for (armorlist <- 0 to armor.length - 1) {
        println("[" + armorlist + "] - " + equ(armor(armorlist)))
      }
      println("Was davon willst du ausrüsten?")
      status = "armor"

    } else {
      println("Du hast leider keine Rüstung zum Ausrüsten!")
    }
    controller.waitInput()
  }

  def armor(act: String, p: Player, equ: ArrayBuffer[Item]) {
    var armor = ArrayBuffer[Int]()
    for (ws <- 0 to equ.length - 1) {
      if (equ(ws).isInstanceOf[Armor]) {
        armor.append(ws)
      }
    }
    if (act != "") {
      act.forall { x => x.isDigit } match {
        case true => {
          if (act.toInt < armor.length) {
            val selected = equ(armor(act.toInt))
            if (selected.isInstanceOf[Armor]) {
              controller.equipArmor(controller.actualPlayer, selected)
            }
            println("Rüstung erfolgreich angelegt!")
            println(p.name + "[" + p.lifePoints + " LP] <" + p.armor + " Rüstung>\n")
          } else {
            println("Du hast eine falsche Zahl eingegeben.")
          }
        }
        case false => println("Du kannst nur eine Zahl eingeben.")
      }
    }
    status = "roundStart"
    printRoundStart
  }

  def manageWeapon(act: String, eq: ArrayBuffer[Item], p: Player) {

    act match {

      case "au" => {

        println("\n********Umrüstung********\n")

        val waffen = controller.availableWeapon(p)
        if (!waffen.isEmpty) {
          for (waffenliste <- 0 to waffen.length - 1) {
            val w = eq(waffen(waffenliste))
            println("[" + waffenliste + "] - {" + w.name + "|" + w.range + " R|" + w.strength + " S" + "}")
          }
          println("Was davon willst du ausrüsten?")

          status = "beweapon"
        } else {
          println("Du hast leider keine Waffen zum Ausrüsten!")
          status = "roundStart"
          printRoundStart
        }
      }

      case "ab" => {

        println("\n********Waffe ablegen********\n")

        controller.beweapon(p, null)
      }
      case _ => {
        println("Falsche Eingabe!")
        status = "roundStart"
        printRoundStart
      }
    }
  }

  def beweapon(act: String, p: Player, eq: ArrayBuffer[Item]) {
    val waffen = controller.availableWeapon(p)
    if (act != "") {
      act.forall { x => x.isDigit } match {
        case true => {
          if (act.toInt < waffen.length) {
            val selected = eq(waffen(act.toInt))
            if (selected.isInstanceOf[Weapon]) {
              controller.beweapon(p, selected)
            }
          } else {
            println("Du hast eine falsche Zahl eingegeben.")
            status = "roundStart"
            printRoundStart
          }
        }
        case false =>
          println("Du kannst nur eine Zahl eingeben.")
          status = "roundStart"
          printRoundStart
      }
    }
  }

  def zombieTurn(z: ArrayBuffer[Zombie]) {
    println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Aktivierung Zombies:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    for (i <- 0 to z.length - 1) {
      controller.zombieTurn(z(i))
      println()
    }
    println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Zusatzaktivierung Runner:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    for (i <- 0 to z.length - 1) {
      if (z(i).name == "Runner") {
        controller.zombieTurn(z(i))
        println()
      }
    }
    println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Zombiekarten werden gezogen:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n")
    val newZombies = controller.drawZombie()
    newZombies.foreach { z => println("Zombie gezogen: " + z.name) }
    println()
  }

  def printEq() {
    val p = controller.actualPlayer
    println("\n\n-----------------------Rucksack-----------------------")
    if (p.equipment.isEmpty) {
      println("   Dein Rucksack ist leer! ")
    }
    for (i <- 0 to p.equipment.length - 1) {
      println("[" + i + "]\t" + p.equipment(i).name)
    }

    println("\n------------------------Rüstung------------------------\n")
    println("Du hast <" + p.armor + "> Rüstung")
    println("\n------------------ausgerüstete Waffe------------------\n")
    val w = p.equippedWeapon
    println("Deine ausgerüstete Waffe ist {" + w.name + "|" + w.range + " R|" + w.strength + " S" + "}\n")
  }

  def playerPos() {
    controller.player.foreach { p => println(p.name + "\t\t (" + p.actualField.p.x / controller.fieldlength + "," + p.actualField.p.y / controller.fieldlength + ")\t" + " [" + p.lifePoints + " LP] <" + p.armor + " Rüstung>") }
  }

  def zombiePos() {
    if (controller.zombies.isEmpty) {
      println("Es befinden sich keine Zombies auf dem Spielfeld!")
    } else {
      controller.zombies.foreach { z => println("Pos (" + z.actualField.p.x / controller.fieldlength + "," + z.actualField.p.y / controller.fieldlength + ")" + " [" + z.lifePoints + " LP]\t" + z.name) }
    }
    println()
  }

}