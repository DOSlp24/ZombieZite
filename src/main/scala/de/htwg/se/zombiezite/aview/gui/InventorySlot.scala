package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.model.{ Item, WeaponInterface, ArmorInterface }
import de.htwg.se.zombiezite.controller._
import scala.swing._
import scala.swing.event._

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
      case MouseClicked(_, _, _, _, _) => c.beweapon(c.actualPlayer, i.asInstanceOf[WeaponInterface])
    }
  } else if (mode == "Armor") {
    reactions += {
      case MouseClicked(_, _, _, _, _) => c.equipArmor(c.actualPlayer, i.asInstanceOf[ArmorInterface])
    }
  }
}