package de.htwg.se.zombiezite.aview

import de.htwg.se.zombiezite.util.Observer
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller.Controller
import scala.io.StdIn
import com.sun.org.apache.xalan.internal.xsltc.compiler.ForEach

class Tui(controller: Controller) extends Observer {

  var fieldlength = 0

  def init() {
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Aufbau::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("Wieviele Spieler spielen mit? (2-4)")
    val playerCount = StdIn.readLine()
    playerCount match {
      case "2" | "3" | "4" => {
        println(playerCount + " Spieler starten.\nLos geht's!\n")
        controller.init(playerCount.toInt)
        fieldlength = controller.fieldlength
        println("Wie lange soll das Spiel gehen?\n[0] Kurz\n[1] Mittel\n[2] Lang")
        val act = StdIn.readLine()
        act match {
          case "0" | "1" | "2" => {
            controller.winCount = (act.toInt + 1) * playerCount.toInt * 15
          }
          case _ => {

          }
        }
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
      case _ => {
        println("Falsche Eingabe : *" + playerCount + "*!")
        System.exit(0)
      }
    }
  }

  def round(runde: Int) {
    println("\n\n°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°")
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Runde " + runde + "::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°~...~°~°\n")
    println("                                                        Zombies getötet: [" + controller.zombiesKilled + "/" + controller.winCount + "]                                                        ")
    println("\n*******************************************************************************************")
    println("::::::::::::::::::::::::::::::::::::::::::::::::Positionen der Spieler [" + controller.playerCount + "]::::::::::::::::::::::::::::::::::::::::::::::::")
    playerPos()
    println("*******************************************************************************************")
    println(":::::::::::::::::::::::::::::::::::::::::::::::Positionen der Zombies [" + controller.zombieCount + "]:::::::::::::::::::::::::::::::::::::::::::::::")
    zombiePos()
    println("*******************************************************************************************\n\n")
    for (i <- 0 to controller.player.length - 1) {
      var p = controller.player(i)
      var eq = p.equipment
      var actionCounter = 3

      while (actionCounter > 0) {

        println("\n\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + p.name + " ist dran - Aktionen übrig [" + actionCounter + "]:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
        println("Was willst du tun " + p.name + "? (b (Bewegung), s (Suche), a (Ausrüstung), an(Angriff), w(Warten))")
        val act = StdIn.readLine()
        act match {

          case "b" => {

            println("\n********Bewegung********\n")

            println("In welche Richtung willst du? *ho(hoch), ru(runter), li(links), re(rechts)*")
            val act = StdIn.readLine()
            actionCounter -= move(act, p)
          }

          case "s" =>
            {

              println("\n********Suche********\n")

              actionCounter -= search(p)
            }

            case"a" => {

              println("\n\n\n\n********Deine Ausrüstung********")

              printEq(p)
              if (!eq.isEmpty) {
                println("\n\nWas willst du damit tun? (w (Waffe wechseln), f (Item fallen lassen), a (Ausrüstung benutzen), x (nichts tun))")
                val act = StdIn.readLine()
                equip(act, p, eq)
              } else {
                println()
              }
            }

            case"an" => {
              println("\nDiese Felder kannst du angreifen:")
              var i = 0
              val af = controller.attackableFields(p.actualField, p.equippedWeapon.range)
              af.foreach { f =>
                println("[" + i + "] " + charsOnField(f))
                i += 1
              }
              println("\n\nIn welches Feld willst du angreifen? ([x] zum Abbrechen.)")
              val act = StdIn.readLine()
              if (act.forall { x => x.isDigit }) {
                if (act.toInt < af.length) {
                  if (p.equippedWeapon.aoe == 1) {
                    if (attackWholeField(p, af(act.toInt))) {
                      actionCounter -= 1
                    } else {
                      System.exit(0)
                    }
                  } else {
                    controller.attackField(p, af(act.toInt)) match {
                      case controller.PLAYER_ATTACK => {
                        println(p.name + " fügte einem " + controller.lastAttackedZombie.typ + " [" + controller.dmg + "] Schaden zu.")
                      }
                      case controller.DEAD => {
                        println(p.name + " tötete einen " + controller.lastAttackedZombie.typ + ".")
                      }
                      case controller.GAME_OVER_WON => {
                        println("\n\n\n|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|")
                        println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
                        println(":_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:::::  SIEG  :::::-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
                        println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
                        println("|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`")
                        System.exit(0)
                      }
                      case controller.FAIL => {
                        actionCounter += 1
                      }
                    }
                    actionCounter -= 1

                  }
                } else {
                  println("Falsche Eingabe: " + act + " ist zu gross!")
                }
              } else if (act == "x") {
                println("Abgebrochen!\n")
              } else {
                println("Falsche Eingabe: " + act + " ist keine Zahl!")
              }
            }

            case"w" => {
              println("\n" + p.name + " setzt sich hin und wartet darauf was passiert.")
              actionCounter = 0
            }
            case _ => println("Absolut falsche Eingabe!")
        }
        println()
      }
    }
    zombieTurn(controller.zombies)
  }

  def attackWholeField(p: Player, f: Field): Boolean = {
    var pCount = f.players.length - 1
    var zCount = f.zombies.length - 1
    while (pCount >= 0) {
      controller.attackPlayerPlayer(p, f.players(pCount)) match {
        case controller.PLAYER_ATTACK_PLAYER => {
          println(p.name + " fügte seinem Verbündeten " + f.players(pCount).name + " [" + controller.dmg + "] Schaden zu!")
        }
        case controller.DEAD => {
          println("AAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHH!!!!\nIch sterbe!!!\n\n")
          println(p.name + " tötete seinen Verbündeten " + controller.lastAttackedPlayer.name + "!")
        }
        case controller.FAIL => {
          println("Angriff auf Verbündeten scheiterte!")
        }
        case controller.GAME_OVER_LOST => {
          println("\n\n\n|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|")
          println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
          println(":_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:::::SELFKILL:::::-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
          println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
          println("|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`")
          System.exit(0)
        }
        case controller.ARMOR_DESTROYED => {
          println(p.name + " zerstörte " + f.players(pCount).name + "'s Rüstung!")
        }
        case controller.ARMOR_DAMAGED => {
          println(p.name + " fügte der Rüstung seines Verbündeten " + f.players(pCount).name + " [" + controller.dmg + "] Schaden zu!")
        }
      }
      pCount -= 1
    }
    while (zCount >= 0) {
      controller.attackZombie(p, f.zombies(zCount)) match {
        case controller.DEAD => {
          println("Ein " + controller.lastAttackedZombie.typ + " weniger!")
        }
        case controller.PLAYER_ATTACK => {
          println(p.name + " fügte " + f.zombies(zCount).typ + " [" + controller.dmg + "] Schaden zu!")
        }
        case controller.FAIL => {
          println("Angriff scheiterte!")
        }
        case controller.GAME_OVER_WON => {
          println("\n\n\n|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|")
          println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
          println(":_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:::::  SIEG  :::::-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
          println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
          println("|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`")
          System.exit(0)
        }
      }
      zCount -= 1
    }
    return true
  }

  def charsOnField(f: Field): String = {
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
      f.zombies.foreach { z => s.append("| " + z.typ + " [" + z.lifePoints + " LP] | ") }
    }
    s.append("]")
    return s.toString()
  }

  def move(act: String, p: Player): Int = {
    act match {

      case "ho" => {
        if (controller.move(p, 0, 1)) {
          println(p.name + " ist ein Feld nach oben gelaufen. Neues Feld (" + p.actualField.p.x / fieldlength + ", " + p.actualField.p.y / fieldlength + ")")
        } else {
          println("Das geht nicht. Nach oben geht es nicht weiter!")
          return 0
        }
      }

      case "ru" => {
        if (controller.move(p, 0, -1)) {
          println(p.name + " ist ein Feld nach unten gelaufen. Neues Feld (" + p.actualField.p.x / fieldlength + ", " + p.actualField.p.y / fieldlength + ")")
        } else {
          println("Das geht nicht. Weiter runter geht es nicht!")
          return 0
        }
      }

      case "li" => {
        if (controller.move(p, -1, 0)) {
          println(p.name + " ist ein Feld nach links gelaufen. Neues Feld (" + p.actualField.p.x / fieldlength + ", " + p.actualField.p.y / fieldlength + ")")
        } else {
          println("Das geht nicht. Nach links ist der Weg blockiert!")
          return 0
        }
      }

      case "re" => {
        if (controller.move(p, 1, 0)) {
          println(p.name + " ist ein Feld nach rechts gelaufen. Neues Feld (" + p.actualField.p.x / fieldlength + ", " + p.actualField.p.y / fieldlength + ")")
        } else {
          println("Das geht nicht. Keine Chance nach rechts zu gehen!")
          return 0
        }
      }

      case _ => {
        println("Falsche Eingabe! Das ist keine Richtung.")
        return 0
      }
    }
    return 1
  }

  def search(p: Player): Int = {
    val tmp = controller.search(p)
    if (tmp != null) {
      println("\n\nDu suchst auf dem aktuellen Feld nach etwas Nützlichem.")
      println("Du hast " + tmp + " gefunden und in deinen Rucksack gepackt")
      println("\n\n***********Deine aktuelle Ausrüstung***********")
      printEq(p)
      println("******************************************")
      println()
      return 1
    } else {
      println("*********Du kannst nicht suchen. Dein Rucksack ist voll*********\n")
      printEq(p)
      return 0
    }
  }

  def equip(act: String, p: Player, eq: ArrayBuffer[Item]) {
    act match {

      case "w" => {

        println("\n********Waffen********\n")

        println("Willst du eine Waffe aus deinem Rucksack ausrüsten (au) oder deine aktuelle ablegen (ab)?")
        val act = StdIn.readLine()
        manageWeapon(act, eq, p)
      }

      case "f" => {

        println("\n********Drop********\n")
        printEq(p)
        println("Welches Item willst du fallen lassen?")
        val act = StdIn.readLine()
        if (canDrop(act, eq)) {
          controller.drop(p, eq(act.toInt))
        }
      }

      case "a" => {

        println("\n********Rüstung********\n")
        manageArmor(eq, p)
      }

      case "x" => println("Selber Schuld.")
      case _ => {
        println("Einfach nö!")
      }
    }
  }

  def canDrop(act: String, eq: ArrayBuffer[Item]): Boolean = {
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
  }

  def manageArmor(eq: ArrayBuffer[Item], p: Player) {
    var armor = ArrayBuffer[Int]()
    for (ws <- 0 to eq.length - 1) {
      if (eq(ws).isInstanceOf[Armor]) {
        armor.append(ws)
      }
    }
    if (!armor.isEmpty) {
      for (armorlist <- 0 to armor.length - 1) {
        println("[" + armorlist + "] - " + eq(armor(armorlist)))
      }
      println("Was davon willst du ausrüsten?")
      val act = StdIn.readLine()
      act.forall { x => x.isDigit } match {
        case true => {
          if (act.toInt < armor.length) {
            val selected = eq(armor(act.toInt))
            if (selected.isInstanceOf[Armor]) {
              p.useArmor(selected)
              p.drop(selected)
            }
            println("Rüstung erfolgreich angelegt!")
            println(p.name + "[" + p.lifePoints + " LP] <" + p.armor + " Rüstung>\n")
          } else {
            println("Du hast eine falsche Zahl eingegeben.")
          }
        }
        case false => println("Du kannst nur eine Zahl eingeben.")
      }
    } else {
      println("Du hast leider keine Rüstung zum Ausrüsten!")
    }
  }

  def manageWeapon(act: String, eq: ArrayBuffer[Item], p: Player) {

    act match {

      case "au" => {

        println("\n********Umrüstung********\n")

        val waffen = controller.availableWeapon(p)
        if (!waffen.isEmpty) {
          for (waffenliste <- 0 to waffen.length - 1) {
            println("[" + waffenliste + "] - " + eq(waffen(waffenliste)))
          }
          println("Was davon willst du ausrüsten?")
          val act = StdIn.readLine()
          act.forall { x => x.isDigit } match {
            case true => {
              if (act.toInt < waffen.length) {
                val selected = eq(waffen(act.toInt))
                if (selected.isInstanceOf[Weapon]) {
                  printBeweapon(controller.beweapon(p, selected), p)
                }
              } else {
                println("Du hast eine falsche Zahl eingegeben.")
              }
            }
            case false => println("Du kannst nur eine Zahl eingeben.")
          }
        } else {
          println("Du hast leider keine Waffen zum Ausrüsten!")
        }
      }

      case "ab" => {

        println("\n********Waffe ablegen********\n")

        printBeweapon(controller.beweapon(p, null), p)
      }
      case _ => {
        println("Falsche Eingabe!")
      }
    }
  }

  def printBeweapon(b: Int, p: Player) {
    b match {
      case controller.DISCARD_WEAPON => {
        println("Ab jetzt kämpft " + p.name + " mit bloßen Fäusten!")
      }
      case controller.CANT_DISCARD_FISTS => {
        println("Du kannst deine Fäuste nicht ablegen!")
      }
      case controller.CANT_DISCARD_FULL_INV => {
        println("Dein Rucksack ist voll. [" + p.equippedWeapon.name + "] passt nicht mehr hinein!")
      }
      case controller.SWAPED_WEAPON => {
        println("Du hast die Waffen getauscht!")
      }
      case controller.EQUIPED_WEAPON => {
        println("Gute Wahl.")
        println("Du hast " + p.equippedWeapon + " ausgerüstet!")
      }
    }
  }

  def zombieTurn(z: ArrayBuffer[Zombie]) {
    println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Aktivierung Zombies:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    for (i <- 0 to z.length - 1) {
      printZombieTurn(controller.zombieTurn(z(i)), z(i))
      println()
    }
    println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Zusatzaktivierung Runner:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    for (i <- 0 to z.length - 1) {
      if (z(i).typ == "Runner") {
        printZombieTurn(controller.zombieTurn(z(i)), z(i))
        println()
      }
    }
    println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Zombiekarten werden gezogen:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n")
    val newZombies = controller.drawZombie()
    newZombies.foreach { z => println("Zombie gezogen: " + z.typ) }
    println()
  }

  def printZombieTurn(zt: Int, z: Zombie) {
    zt match {
      case controller.DEAD => {
        println("AAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHH!\nEin markerschütternder Schrei!\n")
        println("Ein " + z.typ + " hat " + controller.lastAttackedPlayer.name + " getötet!")
      }
      case controller.ZOMBIE_WENT_UP => {
        println("Ein " + z.typ + " ist ein Feld nach oben gelaufen.\tNeues Feld(" + z.actualField.p.x / fieldlength + ", " + z.actualField.p.y / fieldlength + ")")
      }
      case controller.ZOMBIE_WENT_DOWN => {
        println("Ein " + z.typ + " ist ein Feld nach unten gelaufen.\tNeues Feld(" + z.actualField.p.x / fieldlength + ", " + z.actualField.p.y / fieldlength + ")")
      }
      case controller.ZOMBIE_WENT_LEFT => {
        println("Ein " + z.typ + " ist ein Feld nach links gelaufen.\tNeues Feld(" + z.actualField.p.x / fieldlength + ", " + z.actualField.p.y / fieldlength + ")")
      }
      case controller.ZOMBIE_WENT_RIGHT => {
        println("Ein " + z.typ + " ist ein Feld nach rechts gelaufen.\tNeues Feld(" + z.actualField.p.x / fieldlength + ", " + z.actualField.p.y / fieldlength + ")")
      }
      case controller.ZOMBIE_ATTACK => {
        println("Ein " + z.typ + " verursachte [" + controller.dmg + "] Schaden an " + controller.lastAttackedPlayer.name + "!")
      }
      case controller.ARMOR_DAMAGED => {
        println("Ein " + z.typ + " schredderte " + controller.lastAttackedPlayer.name + "s Rüstung für [" + controller.dmg + "] Schaden!")
      }
      case controller.ARMOR_DESTROYED => {
        println("Ein " + z.typ + " zerstörte " + controller.lastAttackedPlayer.name + "s Rüstung!")
      }
      case controller.GAME_OVER_LOST => {
        println("\n\n\n|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|-|v|")
        println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
        println(":_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:::::Game Over:::::-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:-:*:-:_:")
        println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
        println("|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`|_|´`")
        System.exit(0)
      }
      case controller.FAIL => {
        println("Etwas in der Zombie Runde lief schief!")
      }
    }
  }

  def printEq(p: Player) {
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
    println("Deine ausgerüstete Waffe ist [" + p.equippedWeapon + "]\n")
  }

  def playerPos() {
    controller.player.foreach { p => println(p.name + "\t\t (" + p.actualField.p.x / controller.fieldlength + "," + p.actualField.p.y / controller.fieldlength + ")\t" + " [" + p.lifePoints + " LP] <" + p.armor + " Rüstung>") }
  }

  def zombiePos() {
    if (controller.zombies.isEmpty) {
      println("Es befinden sich keine Zombies auf dem Spielfeld!")
    } else {
      controller.zombies.foreach { z => println("Pos (" + z.actualField.p.x / controller.fieldlength + "," + z.actualField.p.y / controller.fieldlength + ")" + " [" + z.lifePoints + " LP]\t" + z.typ) }
    }
    println()
  }

  override def update: Unit = playerPos()
}