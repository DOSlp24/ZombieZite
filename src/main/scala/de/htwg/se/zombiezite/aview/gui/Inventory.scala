package de.htwg.se.zombiezite.aview.gui

import scala.swing._
import scala.swing.event._
import javax.swing._
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller._

class Inventory(c: Controller) extends GridPanel(2, 3) {
  val p = c.actualPlayer

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

  c.actualPlayer.equipment.foreach { i => addItem(i) }

  if (c.actualPlayer.equipment.length < p.EQMAX + 1) {
    for (i <- 1 to p.EQMAX + 1 - c.actualPlayer.equipment.length) {
      addItem(null)
    }
  }

  def addItem(i: Item) {
    if (i != null) {
      i.name match {
        case "EVIL SISTERS" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(sisters) }
        case "Big Mama" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(mama) }
        case "Knife" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(knife) }
        case "Axe" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(axe) }
        case "Pistol" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(pistol) }
        case "Mashine Gun" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(gun) }
        case "Sniper" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(sniper) }
        case "Flame Thrower" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(flame) }
        case "Shotgun" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(shotgun) }
        case "Pan" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(pan) }

        case "Healkit" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(healkit) }
        case "Holy Armor" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(holy) }
        case "Chest" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(chest) }
        case "Boots" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(boots) }
        case "Swat-Shield" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(swat) }

        case "Trash" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(trash) }
        case "Rice" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(rice) }
        case "Bottle" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(bottle) }
        case "Water" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(water) }
        case "Stone" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(stone) }
        case "Canned Food" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(canned) }
        case "Money" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(money) }

        case _ => contents += new InventorySlot(c, i, "") { icon = new ImageIcon(default) }
      }
    } else {
      contents += new Label { icon = new ImageIcon(empty) }
    }
  }
}