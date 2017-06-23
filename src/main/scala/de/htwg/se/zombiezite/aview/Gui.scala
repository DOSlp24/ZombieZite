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
    case e: Fail =>
    case e: ZombieWentUp => update
    case e: ZombieWentDown => update
    case e: ZombieWentRight => update
    case e: ZombieWentLeft => update
    case e: ZombieAttack =>
    case e: DeadPlayer => log.update(e.name + " ist verschieden. " + e.murderer + " hat ihn getötet!\n")
    case e: DeadZombie => {
      log.update(e.name + " hat einen " + e.typ + " erledigt. - Herrlich!\n")
      update
    }
    case e: ItemDropped => update
    case e: Consumed => update
    case e: DiscardWeapon =>
    case e: CantDiscardFists => log.update("Du kannst Fäuste nicht ablegen! - Du bist noch kein Zombie!\n")
    case e: CantDiscardFullInv =>
    case e: SwappedWeapon =>
    case e: EquipedWeapon =>
    case e: ArmorDamaged =>
    case e: ArmorDestroyed =>
    case e: PlayerAttack =>log.update(e.name + " hat einem " + e.typ + " [" + e.dmg + "] Schaden zugefügt. - Großartig!\n")
    case e: PlayerAttackPlayer => log.update(e.atk + " hat " + e.opf + " [" + e.dmg + "] Schaden zugefügt. - Wieso auch immer..\n")
    case e: PlayerMove => update
    case e: ZombieDraw => update
    case e: Wait => update
    case e: StartSpieler => startSpieler
    case e: StartSchwierig => startSchwer
    case e: Start => init
    case e: WaitInput => update
    case e: NewRound => update
    case e: NewAction => update
    case e: Search => update
  }

  def lost = {
    contents_=(Button("LOST"){System.exit(0)})
  }

  def won = {
    contents_=(Button("Congratulations! :)\nYou won!"){System.exit(0)})
  }

  var aLaenge = 0
  var aBreite = 0
  var fields = Array.ofDim[FieldGraphic](aLaenge, aBreite)
  var s = 1000
  var s2 = s / 10
  var grid: GridPanel = null
  val log = new Log()

  def startSpieler {
    val anzahlSpieler = new ComboBox(2 to 4)
    val startGrid = new GridPanel(3, 1) {
      contents += new Label("Wieviele Spieler?")
      contents += anzahlSpieler
      contents += Button("OK") {
        c.init(anzahlSpieler.selection.item)
      }
    }
    contents_=(startGrid)
    visible = true
  }

  def startSchwer {
    val schwierigkeit = new ComboBox(0 to 2)
    val startGrid = new GridPanel(3, 1) {
      contents += new Label("Wie lange soll das Spiel gehen?")
      contents += schwierigkeit
      contents += Button("OK") {
        c.setDifficulty(schwierigkeit.selection.item)
      }
    }
    contents_=(startGrid)
    visible = true
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
      add(new PlayerStat(c),
        constraints(0, 0, fill = GridBagPanel.Fill.Vertical))
      //Game status
      add((new GameStatus(c, log)),
        constraints(2, 0))
      add(new Button("Katsching (0, 2)"),
        constraints(0, 2))
      //Log
      add(grid,
        constraints(1, 0, weightx = 1.0, fill = GridBagPanel.Fill.Horizontal))
      //Field
      add(new Label,
        constraints(1, 1, gridheight = 2, gridwidth = 2,
          fill = GridBagPanel.Fill.Both))
    }
    contents_=(gridBag)
  }

  def init {
    title = "Zombie Zite"
    log.update("************************GAME START************************\n\n\n")
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
    peer.setLocationRelativeTo(null)
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