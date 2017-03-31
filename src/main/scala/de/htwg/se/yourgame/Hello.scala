package de.htwg.se.yourgame

import de.htwg.se.yourgame.model.Area
import de.htwg.se.yourgame.model.Field

object Hello {
  def main(args: Array[String]): Unit = {
    val xTest : Int = 3
    val yTest : Int = 2
    val area = Area(10, 10)
    println(area.zeile.apply(yTest).apply(xTest))
  }
}
