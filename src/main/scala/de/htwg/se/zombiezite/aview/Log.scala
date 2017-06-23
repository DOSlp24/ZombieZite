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

class Log() extends Frame {
  object messages extends TextArea(rows = 40, columns = 60)
  def update(t: String) {
    messages.editable_=(false)
    messages.text_=(messages.text + t)
    contents = new FlowPanel {
      val outputTextScrollPane = new ScrollPane(messages)
      contents += outputTextScrollPane
      contents += Button("Ok") { dispose() }
    }
    def open {
      peer.setLocationRelativeTo(null)
      visible = true
    }
    title = "Log"
  }
}