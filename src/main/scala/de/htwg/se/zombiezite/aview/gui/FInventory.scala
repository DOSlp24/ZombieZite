package de.htwg.se.zombiezite.aview.gui

import de.htwg.se.zombiezite.controller.{ FControllerInterface, cState }
import de.htwg.se.zombiezite.model.{ FItemInterface, Item }
import javax.swing.ImageIcon

import scala.swing.{ GridPanel, Label }

case class FInventory(c: FControllerInterface, state: cState) extends GridPanel(2, 3) {
  val p = state.player(state.actualPlayer)

  val trash = "media/items/Trash.png"
  val bottle = "media/items/Bottle.png"
  val water = "media/items/Water.png"
  val stone = "media/items/Stone.png"
  val rice = "media/items/Rice.png"
  val canned = "media/items/Canned Food.png"
  val money = "media/items/Money.png"

  val healkit = "media/items/Healkit.png"
  val boots = "media/items/Boots.png"
  val chest = "media/items/Chest.png"
  val holy = "media/items/Holy Armor.png"
  val swat = "media/items/Swat-Shield.png"

  val empty = "media/items/Empty.png"
  val default = "media/items/Default.png"

  val sisters = "media/items/EVIL SISTERS.png"
  val mama = "media/items/Big Mama.png"
  val knife = "media/items/Knife.png"
  val axe = "media/items/Axe.png"
  val pistol = "media/items/Pistol.png"
  val pan = "media/items/Pan.png"
  val flame = "media/items/Flame Thrower.png"
  val shotgun = "media/items/Shotgun.png"
  val sniper = "media/items/Sniper.png"
  val gun = "media/items/Mashine Gun.png"

  p.equipment.foreach { i => addItem(i) }

  if (p.gotInventorySpace()) {
    for (i <- 1 to p.EQMAX - p.equipment.length) {
      addItem(null)
    }
  }

  def addItem(i: FItemInterface) {
    if (i != null) {
      i.name match {
        case "EVIL SISTERS" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(sisters) }
        case "Big Mama" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(mama) }
        case "Knife" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(knife) }
        case "Axe" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(axe) }
        case "Pistol" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(pistol) }
        case "Mashine Gun" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(gun) }
        case "Sniper" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(sniper) }
        case "Flame Thrower" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(flame) }
        case "Shotgun" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(shotgun) }
        case "Pan" => contents += new FInventorySlot(c, state, i, "Weapon") { icon = new ImageIcon(pan) }

        case "Healkit" => contents += new FInventorySlot(c, state, i, "Armor") { icon = new ImageIcon(healkit) }
        case "Holy Armor" => contents += new FInventorySlot(c, state, i, "Armor") { icon = new ImageIcon(holy) }
        case "Chest" => contents += new FInventorySlot(c, state, i, "Armor") { icon = new ImageIcon(chest) }
        case "Boots" => contents += new FInventorySlot(c, state, i, "Armor") { icon = new ImageIcon(boots) }
        case "Swat-Shield" => contents += new FInventorySlot(c, state, i, "Armor") { icon = new ImageIcon(swat) }

        case "Trash" => contents += new FInventorySlot(c, state, i, "Trash") { icon = new ImageIcon(trash) }
        case "Rice" => contents += new FInventorySlot(c, state, i, "Trash") { icon = new ImageIcon(rice) }
        case "Bottle" => contents += new FInventorySlot(c, state, i, "Trash") { icon = new ImageIcon(bottle) }
        case "Water" => contents += new FInventorySlot(c, state, i, "Trash") { icon = new ImageIcon(water) }
        case "Stone" => contents += new FInventorySlot(c, state, i, "Trash") { icon = new ImageIcon(stone) }
        case "Canned Food" => contents += new FInventorySlot(c, state, i, "Trash") { icon = new ImageIcon(canned) }
        case "Money" => contents += new FInventorySlot(c, state, i, "Trash") { icon = new ImageIcon(money) }

        case _ => contents += new FInventorySlot(c, state, i, "") { icon = new ImageIcon(default) }
      }
    } else {
      contents += new Label { icon = new ImageIcon(empty) }
    }
  }
}
