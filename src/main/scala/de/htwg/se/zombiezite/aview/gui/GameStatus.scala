package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.controller._
import scala.swing._

class GameStatus(c: Controller, log: Frame) extends GridPanel(3, 1) {

  contents += new Label("Runde " + c.round) {
    font = new Font("Arial", java.awt.Font.HANGING_BASELINE , 15)
  }
  contents += new Label("Zombies getötet: <" + c.zombiesKilled + "/" + c.winCount + ">") {
    font = new Font("Arial", java.awt.Font.HANGING_BASELINE , 15)
  }
  contents += new DPad(c, log)
}