package de.htwg.se.zombiezite

import de.htwg.se.zombiezite.controller._
import de.htwg.se.zombiezite.aview.Tui
import de.htwg.se.zombiezite.aview.Gui
import scala.io.StdIn._
import scala.io.StdIn

object ZombieZiteApp {

  def main(args: Array[String]): Unit = {
    val c = new Controller()

    val gui = new Gui(c)
    val tui = new Tui(c)
    c.publish(new StartSpieler)
    while (true) {
      val a = StdIn.readLine()
      tui.compute(a)
    }
  }
}