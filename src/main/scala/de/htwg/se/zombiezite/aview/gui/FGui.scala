package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.controller._

import scala.swing.{ Button, Dimension, Frame, GridPanel }

class FGui(c: FController) extends Frame {

  val controllerFrame = new Frame()

  listenTo(c)
  reactions += {
    case e: GameOverLost => lost
    case e: GameOverWon => won
    case e: Update => update(e.state)
  }

  def lost {
    contents_=(Button("LOST") {
      System.exit(0)
    })
    deafTo(c)
    repaint
  }

  def won {
    contents_=(Button("Congratulations! :)\nYou won!") {
      System.exit(0)
    })
    deafTo(c)
    repaint
  }

  def update(state: cState) {
    val areaGrid = new GridPanel(state.area.wid, state.area.len) {
      state.area.lines.flatten.foreach(field => {
        contents += FFieldGraphic(field)
      })
    }
    controllerFrame.contents = FDpad(c, state)
    controllerFrame.visible = true
    val grid = new GridPanel(1, 2) {
      contents += areaGrid
    }
    contents = grid
    repaint
  }

  contents_=(Button("Update") {
    c.init()
  })

  preferredSize = new Dimension(1000, 1000)
  visible = true
}
