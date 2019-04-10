package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.controller.{ FControllerInterface, cState }

import scala.swing.{ Button, GridPanel, Label }

case class FDpad(c: FControllerInterface, state: cState) extends GridPanel(5, 3) {
  contents += new Label
  contents += Button("Hoch") { c.move(state, "up") }
  contents += new Label
  contents += Button("Links") { c.move(state, "left") }
  contents += Button("Suchen") { c.search(state) }
  contents += Button("Rechts") { c.move(state, "right") }
  contents += new Label
  contents += Button("Runter") { c.move(state, "down") }
  contents += new Label
  contents += Button("Warten") { c.wait(state) }
  contents += new Label
  //contents += Button("Angriff") { new Attack(c) }
  contents += new Label
  //contents += Button("Log") { log.open() }
  contents += new Label
}
