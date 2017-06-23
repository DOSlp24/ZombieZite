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

class InventorySlot(c: Controller, i: Item, mode: String) extends Label {
  listenTo(this.mouse.clicks)
  listenTo(this.mouse.moves)
  listenTo(this.mouse.wheel)
  reactions += {
    case MouseDragged(_, _, _) => c.drop(c.actualPlayer, i)
    case MouseWheelMoved(_, _, _, _) => new ItemInfo(i, mode)
  }

  if (mode == "Weapon") {
    reactions += {
      case MouseClicked(_, _, _, _, _) => c.beweapon(c.actualPlayer, i)
    }
  } else if (mode == "Armor") {
    reactions += {
      case MouseClicked(_, _, _, _, _) => c.equipArmor(c.actualPlayer, i)
    }
  }
}