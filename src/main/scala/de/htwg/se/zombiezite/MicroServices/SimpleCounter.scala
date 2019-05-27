package de.htwg.se.zombiezite.MicroServices

import scala.io.StdIn

object SimpleCounterMain {
  def main(args: Array[String]): Unit = {
    val webserver = new SimpleCounterServer(new SimpleCounter)
    println(s"Server online at http://localhost:8081/")
    while (true) {
      Thread.sleep(10000000)
    }
    webserver.unbind()
  }
}

class SimpleCounter {

  var counter = 0
  var lastUser = "None Defined"

  def increase(): Unit = {
    counter += 1
  }

  def decrease(): Unit = {
    counter -= 1
  }

  def changeUser(newUser: String): Unit = {
    lastUser = newUser
  }

  override def toString: String = {
    "User " + lastUser + " changed Counter to: " + counter
  }

  def toHtml: String = {
    "<h1>" + toString() + "</h1>"
  }
}
