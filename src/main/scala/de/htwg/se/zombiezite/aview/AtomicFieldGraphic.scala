package de.htwg.se.zombiezite.aview

import swing._
import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._
import javax.swing._
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller._

class AtomicFieldGraphic(s: Dimension, c: Character) extends Label {
  val schlurfi = "media/zombies/Schlurfer.png"
  val spitter = "media/zombies/Spitter.png"
  val tank = "media/zombies/Tank.png"
  val test = "media/zombies/test.png"
  val test2 = "media/zombies/test2.png"
  val test3 = "media/zombies/test3.png"
  val fatti = "media/zombies/Fatti.png"
  val runner = "media/zombies/Runner.png"
  val kawaguchi = "media/players/K. Kawaguchi.png"
  val maiar = "media/players/F. Maiar.png"
  val kaiba = "media/players/H. Kaiba.png"
  val rainbow = "media/players/P. B. Rainbow.png"

  preferredSize = s
  if (c != null) {
    c.name match {
      case "Schlurfer" => {
        icon = new ImageIcon(schlurfi)
      }
      case "Tank" => {
        icon = new ImageIcon(tank)
      }
      case "Spitter" => {
        icon = new ImageIcon(spitter)
      }
      case "Fatti" => {
        icon = new ImageIcon(fatti)
      }
      case "Runner" => {
        icon = new ImageIcon(runner)
      }
      case "F. Maiar" => {
        icon = new ImageIcon(maiar)
      }
      case "K. Kawaguchi" => {
        icon = new ImageIcon(kawaguchi)
      }
      case "H. Kaiba" => {
        icon = new ImageIcon(kaiba)
      }
      case "P. B. Rainbow" => {
        icon = new ImageIcon(rainbow)
      }
      case _ => {
        icon = new ImageIcon(test3)
      }
    }
  } else {
    icon = new ImageIcon
  }

  listenTo(this.mouse.clicks)
  reactions += {
    case MouseClicked(_, _, _, _, _) => {
      icon.toString() match {
        case this.tank => {
          new CharInfo(c, tank, "Zombie")
        }
        case this.schlurfi => {
          new CharInfo(c, schlurfi, "Zombie")
        }
        case this.test => {
          new CharInfo(c, test, "Zombie")
        }
        case this.test2 => {
          new CharInfo(c, test2, "Zombie")
        }
        case this.test3 => {
          new CharInfo(c, test3, "Zombie")
        }
        case this.fatti => {
          new CharInfo(c, fatti, "Zombie")
        }
        case this.runner => {
          new CharInfo(c, runner, "Zombie")
        }
        case this.kawaguchi => {
          new CharInfo(c, kawaguchi, "Player")
        }
        case this.maiar => {
          new CharInfo(c, maiar, "Player")
        }
        case _ => {
          new CharInfo(c, test, "Player")
        }
      }
    }
  }
}