package de.htwg.se.zombiezite.aview

import swing._
import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._
import javax.swing._
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller._

class Inventory(c: Controller) extends GridPanel(2, 3) {
  val p = new Player(null, "default")

  val trash = "media/items/Trash.png"
  val bottle = "media/items/Bottle.png"
  val water = "media/items/Water.png"
  val stone = "media/items/Stone.png"
  val rice = "media/items/Rice.png"
  val canned = "media/items/Canned_Food.png"

  val healkit = "media/items/Healkit.png"

  val empty = "media/items/Empty.png"
  val default = "media/items/Items_Default.png"

  val sisters = "media/items/Evil_Sisters.png"
  val mama = "media/items/Big_Mama.png"
  val knife = "media/items/Knife.png"

  c.actualPlayer.equipment.foreach { i => addItem(i) }

  if (c.actualPlayer.equipment.length < p.EQMAX + 1) {
    for (i <- 1 to p.EQMAX + 1 - c.actualPlayer.equipment.length) {
      addItem(Trash("Empty"))
    }
  }

  def addItem(i: Item) {
    i.name match {
      case "EVIL SISTERS" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(sisters) }
      case "Big Mama" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(mama) }
      case "Knife" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(knife) }
      case "Axe" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(default) }
      case "Pistol" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(default) }
      case "Mashine Gun" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(default) }
      case "Sniper" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(default) }
      case "Flame Thrower" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(default) }
      case "Shotgun" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(default) }
      case "Pan" => contents += new InventorySlot(c, i, "Weapon") { icon = new ImageIcon(default) }
      
      case "Healkit" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(healkit) }
      case "Holy Armor" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(default) }
      case "Chest" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(default) }
      case "Boots" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(default) }
      case "Swat-Shield" => contents += new InventorySlot(c, i, "Armor") { icon = new ImageIcon(default) }
      

      case "Trash" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(trash) }
      case "Rice" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(rice) }
      case "Bottle" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(bottle) }
      case "Water" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(water) }
      case "Stone" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(stone) }
      case "Canned Food" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(canned) }
      case "Money" => contents += new InventorySlot(c, i, "Trash") { icon = new ImageIcon(default) }

      case "Empty" => contents += new Label { icon = new ImageIcon(empty) }

      case _ => contents += new InventorySlot(c, i, "") { icon = new ImageIcon(default) }
    }
  }
}