package de.htwg.se.yourgame.model

case class Student(name: String) {
  def unnoetig(c : String): String = c + c + c + c + c + c + c + "Ayayayayay" + c + c + c + c + c + c + c + c + c + c + c 
  var vorname = name + "-" + unnoetig(Int.int2double(13).toString())
  for (i <- 0 to 1000)
    vorname += unnoetig(unnoetig(unnoetig(unnoetig(Int.int2double(13).toString()))))
  def f(x: Int): Int = x + 1
}