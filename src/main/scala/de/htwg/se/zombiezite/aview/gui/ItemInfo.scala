package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.model.{ Item, WeaponInterface, ArmorInterface }
import de.htwg.se.zombiezite.controller._
import scala.swing._

class ItemInfo(i: Item, mode: String) extends Frame {

  val grid = new GridPanel(3, 1) {
    contents += new Label(i.name) {
      font = new Font("Arial", java.awt.Font.TRUETYPE_FONT, 20)
    }
    if (mode == "Weapon") {
      contents += new Label("Schaden: <" + i.asInstanceOf[WeaponInterface].strength.toString() + "> Reichweite: <" + i.asInstanceOf[WeaponInterface].range.toString() + ">") {
        font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 15)
      }
    }
    if (mode == "Armor") {
      contents += new Label("Rüstung: <" + i.asInstanceOf[ArmorInterface].protection.toString() + ">") {
        font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 15)
      }
    }
    contents += Button("Ok") { dispose() }
  }

  contents_=(grid)
  visible = true
}