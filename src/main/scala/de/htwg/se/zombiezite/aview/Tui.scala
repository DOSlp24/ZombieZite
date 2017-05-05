package de.htwg.se.zombiezite.aview

import de.htwg.se.zombiezite.util.Observer
import scala.collection.mutable.ArrayBuffer
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller.Controller
import scala.io.StdIn

class Tui(controller: Controller) extends Observer {

  def init() {
    println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Aufbau::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    println("Wieviele Spieler spielen mit? (2-4)")
    val playerCount = StdIn.readLine()
    playerCount match {
      case "2" | "3" | "4" => {
        println(playerCount + " Spieler starten.\nLos geht's!\n")
        controller.init(playerCount.toInt)
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
      case _ => println("Falsche Eingabe : *" + playerCount + "*!")
    }
  }

  def round(runde: Int) {
    println("\n\n::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Runde " + runde + "::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::\n")
    println("\n*******************************************************************************************")
    println("::::::::::::::::::::::::::::::::::::::::::::::::Positionen der Spieler::::::::::::::::::::::::::::::::::::::::::::::::")
    controller.playerPos()
    println("*******************************************************************************************")
    println(":::::::::::::::::::::::::::::::::::::::::::::::Positionen der Zombies:::::::::::::::::::::::::::::::::::::::::::::::")
    controller.zombiePos()
    println("*******************************************************************************************\n\n")
    for (i <- 0 to controller.player.length - 1) {
      var p = controller.player(i)
      var eq = p.equipment
      var actionCounter = 3

      while (actionCounter > 0) {

        println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + p.name + " ist dran - Aktionen übrig [" + actionCounter + "]:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
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

              println("\n********Deine Ausrüstung********")

              controller.equip(p)
              if (!eq.isEmpty) {
                println("\n\nWas willst du damit tun? (w (Waffe wechseln), f (fallen lassen), x (nichts))")
                val act = StdIn.readLine()
                equip(act, p, eq)
              } else {
                println("					---				")
                println("Du hast leider nichts in deinem Rucksack.\n")
              }
            }

            case"an" => {
              //TODO implement which Zombies can be attacked
              println("\nDiese Felder kannst du angreifen:")
              attackableFields(p.actualField, p.equippedWeapon.range).foreach { x => println("[" + x.p.x / 2 + "," + x.p.y / 2 + "]") }
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

  def attackableFields(actualField: Field, range: Int): Array[Field] = {
    var attackableFields = ArrayBuffer[Field]()
    for (r <- 0 to range) {
      if (actualField.p.x / 2 + r < controller.area.laenge) {
        attackableFields.append(controller.area.line(actualField.p.x / 2 + r)(actualField.p.y / 2))
      }
      if (actualField.p.x / 2 - r >= 0) {
        attackableFields.append(controller.area.line(actualField.p.x / 2 - r)(actualField.p.y / 2))
      }
    }
    for (r <- 0 to range) {
      if (actualField.p.y / 2 + r < controller.area.laenge) {
        attackableFields.append(controller.area.line(actualField.p.x / 2)(actualField.p.y / 2 + r))
      }
      if (actualField.p.y / 2 - r >= 0) {
        attackableFields.append(controller.area.line(actualField.p.x / 2)(actualField.p.y / 2 - r))
      }
    }
    return attackableFields.distinct.toArray
  }

  def move(act: String, p: Player): Int = {
    act match {

      case "ho" => {
        if (controller.move(p, 0, 1)) {
          println(p.name + " ist ein Feld nach oben gelaufen")
        } else {
          println("Das geht nicht. Nach oben geht es nicht weiter!")
          return 0
        }
      }

      case "ru" => {
        if (controller.move(p, 0, -1)) {
          println(p.name + " ist ein Feld nach unten gelaufen")
        } else {
          println("Das geht nicht. Weiter runter geht es nicht!")
          return 0
        }
      }

      case "li" => {
        if (controller.move(p, -1, 0)) {
          println(p.name + " ist ein Feld nach links gelaufen")
        } else {
          println("Das geht nicht. Nach links ist der Weg blockiert!")
          return 0
        }
      }

      case "re" => {
        if (controller.move(p, 1, 0)) {
          println(p.name + " ist ein Feld nach rechts gelaufen")
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
    if (controller.search(p)) {
      println(".\n.\nDu hast das Item in deinen Rucksack gepackt.")
      println("\n\n***********Deine aktuelle Ausrüstung***********")
      println("Deine ausgerüstete Waffe: " + p.equippedWeapon)
      p.printEq()
      println("******************************************")
      println()
      return 1
    } else {
      println("Du kannst nicht suchen.\nVersuche deine Ausrüstung zu bearbeiten.\n")
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

        println("Welches Item willst du fallen lassen?")
        val act = StdIn.readLine()
        if (canDrop(act, eq)) {
          controller.drop(p, eq(act.toInt))
        }
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

  def manageWeapon(act: String, eq: ArrayBuffer[Item], p: Player) {

    act match {

      case "au" => {

        println("\n********Umrüstung********\n")

        var waffen = ArrayBuffer[Int]()
        for (ws <- 0 to eq.length - 1) {
          if (eq(ws).isInstanceOf[Weapon]) {
            waffen.append(ws)
          }
        }
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
                println("Gute Wahl.")
                controller.beweapon(p, selected)
              }
            } else {
              println("Du hast eine falsche Zahl eingegeben.")
            }
          }
          case false => println("Du kannst nur eine Zahl eingeben.")
        }
      }

      case "ab" => {

        println("\n********Waffe ablegen********\n")

        controller.beweapon(p, null)
      }
      case _ => {
        println("Falsche Eingabe!")
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
      if (z(i).typ == "Runner") {
        controller.zombieTurn(z(i))
        println()
      }
    }
    println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::Zombiekarte wird gezogen:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::")
    controller.drawZombie()
  }

  override def update: Unit = controller.playerPos()
}