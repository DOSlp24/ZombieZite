package de.htwg.se.zombiezite.CustomTypes

import de.htwg.se.zombiezite.controller.cState

class CStateMonad[T](val states: Seq[cState]) {
  //  def map(s: T => T): Seq[T] = states.map(state => s(state))
}

/*trait Option[cState] {
  def map(f: cState => cState): Option[cState]
}

case class Some[cState](b: cState) extends Option[cState] {
  def map(f: cState => cState) = Some(f(b))
}

case class None[cState]() extends Option[cState] {
  def map(f: cState => cState) = new None
}*/ 