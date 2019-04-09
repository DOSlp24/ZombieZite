package de.htwg.se.zombiezite.aview.gui

import java.awt.Dimension

import de.htwg.se.zombiezite.model.FFieldInterface

import scala.swing.{ GridPanel, Label, TextField }

case class FFieldGraphic(field: FFieldInterface) extends GridPanel(1, 1) {
  val height = 100
  if (field.chars.isEmpty) {
    contents += new TextField() {
      editable_=(false)
    }
  } else {
    val f = new GridPanel(2, 2) {
      field.chars.foreach(c => contents += AtomicFieldGraphic(new Dimension(height / 2, height / 2), c))
      // contents ++ field.chars.map(c => AtomicFieldGraphic(new Dimension(height / 2, height / 2), c))
    }
    contents += f
  }
}
