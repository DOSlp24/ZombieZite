package de.htwg.se.zombiezite.util

import de.htwg.se.zombiezite.controller.{ FController, cState }
import de.htwg.se.zombiezite.model.baseImpl._
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }
import scala.runtime.BoxedUnit
import scala.util.{ Failure, Success }

case class SlickDAO() {
  val db = Database.forConfig("zombieDb")

  val areaTable = TableQuery[AreaTable]

  val fieldTable = TableQuery[FieldTable]

  val playerTable = TableQuery[PlayerTable]

  val zombieTable = TableQuery[ZombieTable]

  val weaponTable = TableQuery[WeaponTable]

  val armorTable = TableQuery[ArmorTable]

  val trashTable = TableQuery[TrashTable]

  def create(state: cState): Future[Unit] = {
    try {

      // A Query on a specific Table (Area in this case) - We need it for foreign keys and using the db

      val setup = DBIO.seq(
        (areaTable.schema ++ fieldTable.schema ++ playerTable.schema ++ zombieTable.schema ++ weaponTable.schema
        ++ armorTable.schema ++ trashTable.schema).create,

        areaTable += (0, state.area.len, state.area.wid), // our area
        areaTable += (1, 5, 5),
        fieldTable ++= state.area.lines.zipWithIndex.flatMap {
          case (line, lineIndex) => line.zipWithIndex.map {
            case (f, i) => (lineIndex * state.area.len + i, f.p.x, f.p.y, 0, f.charCount)
          }
        },
        playerTable ++= state.player.map(p => (p.name, p.y * 10 + p.x, p.lifePoints, p.armor, p.strength, p.range)) //Map all players to a sequence
      )
      val setupFuture = db.run(setup)

      setupFuture
    }
  }

  def read(): cState = {
    val retState: cState = new FController().init()
    val newLines = retState.area.lines.zipWithIndex.map {
      case (line, lineIndex) => {
        line.zipWithIndex.map {
          case (field: FField, fieldIndex) => {
            val fieldId = lineIndex * retState.area.len + fieldIndex
            val newCharCount = Await.result(db.run(fieldTable.filter(f => f.id === fieldId)
              .map(_.charCount).result.head), Duration.Inf)

            val newPlayers = Await.result(db.run(playerTable
              .filter(p => p.fieldId === fieldId)
              .result), Duration.Inf).map(p => FPlayer(fieldIndex, lineIndex, p._3, p._4, p._6, p._5, p._1)) //Other possibility?

            field.copy(charCount = newCharCount, players = newPlayers.toVector, chars = newPlayers.toVector)
          }
        }
      }
    }

    val newArea = FArea(newLines.length, newLines.length)
    retState.copy(area = newArea.copy(lines = newLines)).updateChars()
  }

  def update(table: String, state: cState, id: Int = 0): Future[Int] = {
    table match {
      case "area" => db.run(areaTable.update(id, state.area.len, state.area.wid))
      case "player" => db.run(playerTable
        .update((state.player(id).name, state.player(id).y * 10 + state.player(id).x, state.player(id).lifePoints, state.player(id).armor, state.player(id).strength, state.player(id).range)))
    }
  }

  def delete(table: String, id: Int = 0, name: String = ""): Future[Int] = {
    table match {
      case "area" => db.run(areaTable.filter(f => f.id === id).delete)
      case "field" => db.run(fieldTable.filter(f => f.id === id).delete)
      case "player" => db.run(playerTable.filter(f => f.name === name).delete)
    }
  }
}
