package de.htwg.se.zombiezite.aview

import swing._
import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._
import javax.swing._
import de.htwg.se.zombiezite.model._

class FieldGraphic(s: Dimension) extends GridPanel(1, 1) {
  var zombies: Array[Zombie] = new Array(0)
  var players: Array[Player] = new Array(0)
  var attackMode = false
  //  val schlurfi = "Z:\\SWT\\workspace\\media\\zombies\\Schlurfer.png"
  //  val tank = "Z:\\SWT\\workspace\\media\\zombies\\Tank.png"
  //  val test = "Z:\\SWT\\workspace\\media\\zombies\\test.png"
  //  val test2 = "Z:\\SWT\\workspace\\media\\zombies\\test2.png"
  //  val test3 = "Z:\\SWT\\workspace\\media\\zombies\\test3.png"
  //  val fatti = "Z:\\SWT\\workspace\\media\\zombies\\Fattie.png"
  //  val runner = "Z:\\SWT\\workspace\\media\\zombies\\Runner.png"
  val schlurfi = "Z:/SWT/workspace/media/zombies/Schlurfer.png"
  val tank = "Z:/SWT/workspace/media/zombies/Tank.png"
  val test = "Z:/SWT/workspace/media/zombies/test.png"
  val test2 = "Z:/SWT/workspace/media/zombies/test2.png"
  val test3 = "Z:/SWT/workspace/media/zombies/test3.png"
  val fatti = "Z:/SWT/workspace/media/zombies/Fatti.png"
  val runner = "Z:/SWT/workspace/media/zombies/Runner.png"
  preferredSize = s
  def update {
    this.contents.clear()
    if (zombies.isEmpty && players.isEmpty) {
      contents += new Label
    } else {
      if (zombies.length <= 4 && players.isEmpty) {
        val field = new GridPanel(2, 2) {
          zombies.length match {
            case 1 => {
              val field = new Label {
                contents += new AtomicFieldGraphic(s, zombies(0))
              }
            }
            case 2 => {
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(0))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(1))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
            }
            case 3 => {
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(0))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(1))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(2))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
            }
            case 4 => {
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(0))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(1))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(2))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), zombies(3))
            }
            case _ =>
          }
        }
        contents += field
      } else if (zombies.isEmpty) {
        val field = new GridPanel(2, 2) {
          players.length match {
            case 1 => {
              contents += new AtomicFieldGraphic(s, players(0))
            }
            case 2 => {
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(0))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(1))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
            }
            case 3 => {
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(0))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(1))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(2))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
            }
            case 4 => {
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(0))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(1))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(2))
              contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), players(3))
            }
            case _ =>
          }
        }
        contents += field
      } else {
        if (players.length + zombies.length > 4) {
          val field = new Label {
            //TODO define
            text = "More than 4 Chars here!"
          }
          contents += field
        } else {
          val field = new GridPanel(2, 2) {
            val chars = players ++ zombies
            chars.length match {
              case 2 => {
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(0))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(1))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
              }
              case 3 => {
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(0))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(1))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(2))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), null)
              }
              case 4 => {
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(0))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(1))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(2))
                contents += new AtomicFieldGraphic(new Dimension(s.height / 2, s.width / 2), chars(3))
              }
              case _ =>
            }
          }
          contents += field
        }
      }
    }
  }
}