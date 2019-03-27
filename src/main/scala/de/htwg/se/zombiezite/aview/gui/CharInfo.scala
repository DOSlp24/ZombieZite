package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.model.Character
import de.htwg.se.zombiezite.controller._
import scala.swing._
import javax.swing.ImageIcon

class CharInfo(c: Character, iconPath: String, mode: String) extends Frame {

  val grid = new GridPanel(10, 1) {
    if (mode == "Zombie") {
      contents += new Label("^*^*^*^*" + c.name + "^*^*^*^*") {
        font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 20)
      }
    } else if (mode == "Player") {
      contents += new Label("*********" + c.name + "*********") {
        font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 20)
      }
    }
    contents += new Label {
      icon = new ImageIcon(iconPath)
    }
    contents += new Label("Lifepoints: *" + c.lifePoints.toString() + "*") {
      font = new Font("Arial", java.awt.Font.TRUETYPE_FONT, 20)
    }
    contents += new Label("Schaden: <" + (c.strength + c.equippedWeapon.strength).toString() + "> Reichweite : <" + (c.range + c.equippedWeapon.range).toString() + ">") {
      font = new Font("Arial", java.awt.Font.TRUETYPE_FONT, 20)
    }
    contents += Button("Ok") { dispose() }
  }
  contents_=(grid)
  visible = true
}