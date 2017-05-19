package de.htwg.se.zombiezite.aview

import de.htwg.se.zombiezite.util.Observer
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller._
import swing._
import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._

class Gui(c: Controller) extends Frame {

  title = "Zombie Zite"

  title = "Hello, World!"

  val label =
    new Label {
      text = "Hello World"
      font = new Font("Verdana", 1, 36)
    }
  
  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New") { System.console().flush() })
      contents += new MenuItem(Action("Quit") { System.exit(0) })
    }
  }

  val cell = new BoxPanel(Orientation.Vertical) {
    contents += label
  }

  contents = menuBar

    visible = true
}