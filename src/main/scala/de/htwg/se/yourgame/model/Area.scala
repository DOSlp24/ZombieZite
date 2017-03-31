package de.htwg.se.yourgame.model

case class Area(breite: Int, laenge: Int) {
  //Array Definition für zeilen aus reihen
  var zeile = Array.ofDim[Field](laenge, breite)

  //Startpunkt des Spielfeldes
  val x: Int = 0
  val y: Int = 0

  //Step Definition und Field-Standardobjekt
  val feld: Field = Field(Position(0, 0))
  val step: Int = feld.length

  //Spielfeldaufbau
  for (i <- 0 to laenge - 1) {
    for (j <- 0 to breite - 1) {
      zeile(j)(i) = Field(Position(x + step * j, y + step * i))
    }
  }
}