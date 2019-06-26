package de.htwg.se.zombiezite.util

import de.htwg.se.zombiezite.controller.cState

import scala.concurrent.Future

trait DAOTrait {
  def create(state: cState): Future[Unit]

  def read(): cState

  def update(table: String, state: cState, id: Int = 0): Future[Int]

  def delete(table: String, id: Int = 0, name: String = ""): Future[Int]
}
