package de.htwg.se.zombiezite.model

trait Deck[T] {
  def draw(): T
}