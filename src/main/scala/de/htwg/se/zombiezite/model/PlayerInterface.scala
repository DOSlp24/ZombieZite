package de.htwg.se.zombiezite.model

trait PlayerInterface extends Character{
  def equip(item: Item): Boolean
  
  def useArmor(a: Item): Boolean 

  def drop(item: Item): Item 
}