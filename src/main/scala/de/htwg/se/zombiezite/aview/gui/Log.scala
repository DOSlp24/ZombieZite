package de.htwg.se.zombiezite.aview.gui

import scala.swing._

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