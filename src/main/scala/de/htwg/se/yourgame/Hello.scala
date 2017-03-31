package de.htwg.se.yourgame

import de.htwg.se.yourgame.model.Student

object Hello {
  def main(args: Array[String]): Unit = {
    val student = Student(Int.int2double(13).toString())
    println("Hello, " + student.name)
    println("Vorname, " + student.vorname.toUpperCase())
    student.vorname += Int.int2double(13).toString()
    println("Vorname, " + student.vorname)
    println(student.f(3))
  }
}
