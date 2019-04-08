package de.htwg.se.zombiezite

import de.htwg.se.zombiezite.aview.gui.{ FGui, Gui }
import de.htwg.se.zombiezite.aview.tui.Tui
import de.htwg.se.zombiezite.controller._
import de.htwg.se.zombiezite.model.FItemInterface
import de.htwg.se.zombiezite.model.baseImpl.FItemDeck

import scala.io.StdIn

object ZombieZiteApp {

  //val c = new Controller()
  val c = new FController()

  def main(args: Array[String]): Unit = {
    val gui = new FGui(c)
    /*val gui = new Gui(c)
    val tui = new Tui(c)
    c.publish(new StartSpieler)
    while (true) {
      val a = StdIn.readLine()
      tui.compute(a)
    }*/
  }

  /*def getController(): Controller = {
    return c
  }*/
}