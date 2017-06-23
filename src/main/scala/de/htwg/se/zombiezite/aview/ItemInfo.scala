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

class ItemInfo(i: Item, mode: String) extends Frame {

  val grid = new GridPanel(3, 1) {
    contents += new Label(i.name) {
      font = new Font("Arial", java.awt.Font.TRUETYPE_FONT, 20)
    }
    if (mode == "Weapon") {
      contents += new Label("Schaden: <" + i.strength.toString() + "> Reichweite: <" + i.range.toString() + ">") {
        font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 15)
      }
    }
    if (mode == "Armor") {
      contents += new Label("Rüstung: <" + i.protection.toString() + ">") {
        font = new Font("Arial", java.awt.Font.HANGING_BASELINE, 15)
      }
    }
    contents += Button("Ok") { dispose() }
  }

  contents_=(grid)
  visible = true
}