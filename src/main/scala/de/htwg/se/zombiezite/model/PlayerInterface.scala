package de.htwg.se.zombiezite.model

import scala.collection.mutable.ArrayBuffer

trait PlayerInterface extends Character{
  val EQMAX = 5
  var equipment: ArrayBuffer[Item] = null
  
  def equip(item: Item): Boolean
  
  def useArmor(a: ArmorInterface): Boolean 

  def drop(item: Item): Item 
}