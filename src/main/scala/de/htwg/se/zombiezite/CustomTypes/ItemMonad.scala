package de.htwg.se.zombiezite.CustomTypes

class ItemMonad[T](val items: Seq[T]) {
  def map(f: T => T): Seq[T] = items.map(item => f(item))
}

trait Option[FItemInterface] {
  def map(f: FItemInterface => FItemInterface): Option[FItemInterface]
}

case class Some[FItemInterface](b: FItemInterface) extends Option[FItemInterface] {
  def map(f: FItemInterface => FItemInterface) = Some(f(b))
}

case class None[FItemInterface]() extends Option[FItemInterface] {
  def map(f: FItemInterface => FItemInterface) = new None
}
