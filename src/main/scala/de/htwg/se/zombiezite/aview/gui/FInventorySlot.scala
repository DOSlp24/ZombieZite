package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.controller.{ FControllerInterface, cState }
import de.htwg.se.zombiezite.model._

import scala.swing.Label
import scala.swing.event.{ MouseClicked, MouseDragged, MouseWheelMoved }

case class FInventorySlot(c: FControllerInterface, state: cState, i: FItemInterface, mode: String) extends Label {
  val p = state.player(state.actualPlayer)

  listenTo(this.mouse.clicks)
  listenTo(this.mouse.moves)
  listenTo(this.mouse.wheel)
  reactions += {
    case MouseDragged(_, _, _) => c.drop(state, i)
    //case MouseWheelMoved(_, _, _, _) => new ItemInfo(i, mode)
  }

  if (mode == "Weapon") {
    reactions += {
      case MouseClicked(_, _, _, _, _) => c.beweapon(state, i.asInstanceOf[FWeaponInterface])
    }
  } else if (mode == "Armor") {
    reactions += {
      case MouseClicked(_, _, _, _, _) => c.equipArmor(state, i.asInstanceOf[FArmorInterface])
    }
  }
}
