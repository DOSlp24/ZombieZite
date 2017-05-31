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

class GameStatus(killed: Int, round: Int) extends GridPanel(2, 1){
  contents += new TextField{
    text = "Runde " + round
    editable_=(false)
  }
  contents += new TextField{
    text = "Zombies getötet: <" + killed + ">"
    rows_=(5)
    editable_=(false)
  }
}