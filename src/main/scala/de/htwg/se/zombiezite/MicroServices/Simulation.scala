package de.htwg.se.zombiezite.MicroServices

import de.htwg.se.zombiezite.controller.FController

import scala.io.StdIn

class Simulation(c: FController) {
  def start(): Unit = {
    val webserver = new SimulationServer(c)
    println(s"Server online at http://localhost:8002/")
    while (true) {
      Thread.sleep(10000000)
    }
    webserver.unbind()
  }
}
