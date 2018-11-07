package de.htwg.se.zombiezite.aview.gui

import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import javax.swing._
import de.htwg.se.zombiezite.model.Character
import de.htwg.se.zombiezite.controller._
import scala.util.parsing.json._
import scala.io.Source

class AtomicFieldGraphic(s: Dimension, c: Character) extends Label {
  //TODO Define error case here
  val schlurfi, spitter, tank, fatti, runner, kawaguchi, maiar, kaiba, rainbow, test, test2, test3 = "media/zombies/Default.png"
  val filename = "constants/MediaCon.json"

  val conModel = Source.fromFile(filename).mkString
  val json = JSON.parseFull(conModel)
  json match {
    case Some(e) => {
      val map = e.asInstanceOf[Map[String, Map[String, String]]]
      val schlurfi = map.get("schlurfi").get("source")
      val spitter = map.get("spitter").get("source")
      val tank = map.get("tank").get("source")
      val runner = map.get("runner").get("source")
      val fatti = map.get("fatti").get("source")
      val kawaguchi = map.get("kawaguchi").get("source")
      val maiar = map.get("maiar").get("source")
      val kaiba = map.get("kaiba").get("source")
      val rainbow = map.get("rainbow").get("source")
      val test = map.get("test").get("source")
      val test2 = map.get("test2").get("source")
      val test3 = map.get("test3").get("source")
      preferredSize = s
      if (c != null) {
        c.name match {
          case "Schlurfer" => {
            icon = new ImageIcon(schlurfi)
          }
          case "Tank" => {
            icon = new ImageIcon(tank)
          }
          case "Spitter" => {
            icon = new ImageIcon(spitter)
          }
          case "Fatti" => {
            icon = new ImageIcon(fatti)
          }
          case "Runner" => {
            icon = new ImageIcon(runner)
          }
          case "F. Maiar" => {
            icon = new ImageIcon(maiar)
          }
          case "K. Kawaguchi" => {
            icon = new ImageIcon(kawaguchi)
          }
          case "H. Kaiba" => {
            icon = new ImageIcon(kaiba)
          }
          case "P. B. Rainbow" => {
            icon = new ImageIcon(rainbow)
          }
          case _ => {
            icon = new ImageIcon(test3)
          }
        }
      } else {
        icon = new ImageIcon
      }

      listenTo(this.mouse.clicks)
      reactions += {
        case MouseClicked(_, _, _, _, _) => {
          icon.toString() match {
            case this.tank => {
              new CharInfo(c, tank, "Zombie")
            }
            case this.schlurfi => {
              new CharInfo(c, schlurfi, "Zombie")
            }
            case this.test => {
              new CharInfo(c, test, "Zombie")
            }
            case this.test2 => {
              new CharInfo(c, test2, "Zombie")
            }
            case this.test3 => {
              new CharInfo(c, test3, "Zombie")
            }
            case this.fatti => {
              new CharInfo(c, fatti, "Zombie")
            }
            case this.runner => {
              new CharInfo(c, runner, "Zombie")
            }
            case this.kawaguchi => {
              new CharInfo(c, kawaguchi, "Player")
            }
            case this.maiar => {
              new CharInfo(c, maiar, "Player")
            }
            case _ => {
              new CharInfo(c, test, "Player")
            }
          }
        }
      }
    }
    case None => println("Failed")
  }

}