package de.htwg.se.zombiezite.aview

import de.htwg.se.zombiezite.util.Observer
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller._
import swing._
import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._
import javax.swing._

class PlayerStat(c: Controller) extends GridPanel(10, 1) {
  val axe = "media/weapons/Axe.png"
  val fists = "media/weapons/Fists.png"

  val maiar = "media/players/F. Maiar Por.png"
  val kawaguchi = "media/players/K. Kawaguchi Por.png"

  val default = "media/weapons/Default.png"

  contents += new Label {
    c.actualPlayer.name match {
      case "F. Maiar" => {
        icon = new ImageIcon(maiar)
      }
      case "K. Kawaguchi" => {
        icon = new ImageIcon(kawaguchi)
      }
      case "H. Kaiba" => {
        icon = new ImageIcon(default)
      }
      case "P. B. Rainbow" => {
        icon = new ImageIcon(default)
      }
      case _ => {
        icon = new ImageIcon(default)
      }
    }
  }
  contents += new Label(c.actualPlayer.name + " ist am Zug.") {
    font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 15)
  }
  contents += new Label {
    listenTo(this.mouse.wheel)
    listenTo(this.mouse.clicks)
    reactions += {
      case MouseWheelMoved(_, _, _, _) => new ItemInfo(c.actualPlayer.equippedWeapon, "Weapon")
      case MouseClicked(_, _, _, _,_) => c.beweapon(c.actualPlayer, null)
    }
    c.actualPlayer.equippedWeapon.name match {
      case "Axe" => icon = new ImageIcon(axe)
      case "Fist" => icon = new ImageIcon(fists)
      case _ => icon = new ImageIcon(default)
    }
  }
  contents += new Inventory(c)
  contents += new Label {
    contents += new Label("LP " + c.actualPlayer.lifePoints.toString()) {
      font = new Font("Arial", java.awt.Font.TRUETYPE_FONT, 20)
    }
    contents += new Label("Rüstung " + c.actualPlayer.armor.toString()) {
      font = new Font("Arial", java.awt.Font.TRUETYPE_FONT, 20)
    }
  }
  contents += new Label("Aktionspunkte: " + c.actualPlayer.actionCounter) {
    font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 15)
  }
}