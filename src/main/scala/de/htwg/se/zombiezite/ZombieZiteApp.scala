package de.htwg.se.zombiezite

import de.htwg.se.zombiezite.controller._
import de.htwg.se.zombiezite.aview.Tui
import de.htwg.se.zombiezite.aview.Gui
import scala.io.StdIn._

object ZombieZite {
  val controller = new Controller()

  val gui = new Gui(controller)
  val tui = new Tui(controller)
  controller.publish(new StartSpieler)

  def main(args: Array[String]): Unit = {
    while (true) {
      do {
        loopPlayer
        if (controller.actualPlayer != controller.player.last) {
          controller.actualPlayer = controller.player(controller.player.indexOf(controller.actualPlayer) + 1)
        }
      } while (controller.actualPlayer != controller.player.last)
        loopPlayer
      //      tui.zombieTurn(controller.zombies)
      controller.fullZombieTurn
      controller.newRound
      controller.roundReset()
    }
  }
  
  def loopPlayer{
    while (controller.actualPlayer.actionCounter > 0) {
          controller.waitInput()
        }
  }
}