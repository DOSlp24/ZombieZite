package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.controller._
import scala.swing._
import scala.io.Source._

class DPad(c: Controller, log: Frame) extends GridPanel(5, 3) {
  contents += new Label
  contents += Button("Hoch") { c.move(c.actualPlayer, 0, 1) }
  contents += new Label
  contents += Button("Links") { c.move(c.actualPlayer, -1, 0) }
  contents += Button("Suchen") { c.search(c.actualPlayer) }
  contents += Button("Rechts") { c.move(c.actualPlayer, 1, 0) }
  contents += new Label
  contents += Button("Runter") { c.move(c.actualPlayer, 0, -1) }
  contents += new Label
  contents += Button("Warten") { c.wait(c.actualPlayer) }
  contents += new Label
  contents += Button("Angriff") { new Attack(c) }
  contents += new Label
  contents += Button("Log") { log.open() }
  contents += new Label
}