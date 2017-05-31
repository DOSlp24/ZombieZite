package de.htwg.se.zombiezite.aview

import de.htwg.se.zombiezite.util.Observer
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.controller._
import swing._
import scala.swing._
import scala.swing.Swing.LineBorder
import scala.swing.event._
import scala.io.Source._
import javax.swing._

class Gui(c: Controller) extends Frame {

  listenTo(c)
  reactions += {
    case e: GameOverLost => lost
    case e: GameOverWon => won
    case e: Fail => {
    }
    case e: ZombieWentUp => {
      update
    }
    case e: ZombieWentDown => {
      update
    }
    case e: ZombieWentRight => {
      update
    }
    case e: ZombieWentLeft => {
      update
    }
    case e: ZombieAttack => {
    }
    case e: DeadPlayer => {
    }
    case e: DeadZombie => {
      update
    }
    case e: DiscardWeapon => {
    }
    case e: CantDiscardFists => {
    }
    case e: CantDiscardFullInv => {
    }
    case e: SwappedWeapon => {
    }
    case e: EquipedWeapon => {
    }
    case e: ArmorDamaged => {
    }
    case e: ArmorDestroyed => {
    }
    case e: PlayerAttack => {
    }
    case e: PlayerAttackPlayer => {
    }
    case e: PlayerMove => {
      update
    }
    case e: ZombieDraw => {
      update
    }
    case e: Wait => {
      update
    }
    case e: StartSpieler => {

    }
    case e: StartSchwierig => {

    }
    case e: Start => {
      init
    }
    case e: WaitInput => {

    }
  }

  def lost = {
    //System.exit(0)
  }

  def won = {
    //System.exit(0)
  }

  title = "Zombie Zite"
  var aLaenge = 0
  var aBreite = 0
  var fields = Array.ofDim[FieldGraphic](aLaenge, aBreite)
  var s = 1000
  var s2 = s / 10
  var grid: GridPanel = null
  val button = new Button("Warten")

  listenTo(button)
  reactions += {
    case ButtonClicked(button) => {
      c.wait(c.actualPlayer)
    }
  }

  def areaGraphic() {
    grid = new GridPanel(aLaenge, aBreite) {

      preferredSize = new Dimension(s, s)
      resizable_=(false)
      fields.foreach { line =>
        line.foreach { field =>
          contents += field
        }
      }
    }
    windowRepaint()
  }

  def windowRepaint() {
    val gridBag = new GridBagPanel() {
      def constraints(x: Int, y: Int,
        gridwidth: Int = 1, gridheight: Int = 1,
        weightx: Double = 0.0, weighty: Double = 0.0,
        fill: GridBagPanel.Fill.Value = GridBagPanel.Fill.None): Constraints = {
        val c = new Constraints
        c.gridx = x
        c.gridy = y
        c.gridwidth = gridwidth
        c.gridheight = gridheight
        c.weightx = weightx
        c.weighty = weighty
        c.fill = fill
        c
      }
      //Player status panel
      add(new Label("Label @ (0,0)") { border = Swing.EtchedBorder(Swing.Lowered) },
        constraints(0, 0, gridheight = 2, fill = GridBagPanel.Fill.Both))
      //Game status
      add((new GameStatus(c.zombiesKilled, c.round)),
        constraints(2, 0))
      add(button,
        constraints(2, 1))
      add(new Button("Button @ (2,2)"),
        constraints(2, 2))
      add(new CheckBox("Check me!"),
        constraints(0, 2))
      //Log
      add(grid ,
        constraints(1, 0, weightx = 1.0, fill = GridBagPanel.Fill.Horizontal))
      //Field
      add(new Label,
        constraints(1, 1, gridheight = 2, weighty = 1.0,
          fill = GridBagPanel.Fill.Both))
      add(Button("Close") { sys.exit(0) },
        constraints(0, 4, gridwidth = 3, fill = GridBagPanel.Fill.Horizontal))
    }
    contents_=(gridBag)
  }

  def init {
    aLaenge = c.area.line.length
    aBreite = c.area.line(0).length
    fields = Array.ofDim[FieldGraphic](aLaenge, aBreite)
    for (b <- 0 to aLaenge - 1) {
      for (a <- 0 to aBreite - 1) {
        fields(a)(b) = new FieldGraphic(new Dimension(s2, s2))
        fields(a)(b).players = c.area.line(b)(aBreite - a - 1).players.toArray
        fields(a)(b).zombies = c.area.line(b)(aBreite - a - 1).zombies.toArray
        fields(a)(b).update
      }
    }
    areaGraphic()
    visible = true
  }

  def update {
    for (b <- 0 to aLaenge - 1) {
      for (a <- 0 to aBreite - 1) {
        fields(a)(b).players = c.area.line(b)(aBreite - a - 1).players.toArray
        fields(a)(b).zombies = c.area.line(b)(aBreite - a - 1).zombies.toArray
        fields(a)(b).update
      }
    }
    areaGraphic()
  }
}