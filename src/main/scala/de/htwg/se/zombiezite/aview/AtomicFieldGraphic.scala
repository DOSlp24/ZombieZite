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
  //  val schlurfi = "Z:\\SWT\\workspace\\media\\zombies\\Schlurfer.png"
  //  val tank = "Z:\\SWT\\workspace\\media\\zombies\\Tank.png"
  //  val test = "Z:\\SWT\\workspace\\media\\zombies\\test.png"
  //  val test2 = "Z:\\SWT\\workspace\\media\\zombies\\test2.png"
  //  val test3 = "Z:\\SWT\\workspace\\media\\zombies\\test3.png"
  //  val fatti = "Z:\\SWT\\workspace\\media\\zombies\\Fattie.png"
  //  val runner = "Z:\\SWT\\workspace\\media\\zombies\\Runner.png"
  val schlurfi = "../media/zombies/Schlurfer.png"
  val tank = "../media/zombies/Tank.png"
  val test = "../media/zombies/test.png"
  val test2 = "../media/zombies/test2.png"
  val test3 = "../media/zombies/test3.png"
  val fatti = "../media/zombies/Fatti.png"
  val runner = "../media/zombies/Runner.png"
  val kawaguchi = "../media/players/Kawaguchi.png"
  val maiar = "../media/players/Maiar.png"

  preferredSize = s
  if (c != null) {
    c.name match {
      case "Schlurfi" => {
        icon = new ImageIcon(schlurfi)
      }
      case "Tank" => {
        icon = new ImageIcon(tank)
      }
      case "Spitter" => {
        icon = new ImageIcon(test)
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
        icon = new ImageIcon(test2)
      }
      case "P. B. Rainbow" => {
        icon = new ImageIcon(test2)
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
          icon = new ImageIcon(schlurfi)
        }
        case this.schlurfi => {
          icon = new ImageIcon(test)
        }
        case this.test => {
          icon = new ImageIcon(test2)
        }
        case this.test2 => {
          icon = new ImageIcon(test3)
        }
        case this.test3 => {
          icon = new ImageIcon(fatti)
        }
        case this.fatti => {
          icon = new ImageIcon(runner)
        }
        case this.runner => {
          icon = new ImageIcon(tank)
        }
        case _ => {
          icon = new ImageIcon(schlurfi)
        }
      }
    }
  }
}