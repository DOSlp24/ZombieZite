package de.htwg.se.zombiezite.util

import de.htwg.se.zombiezite.controller.cState
import de.htwg.se.zombiezite.model._
import de.htwg.se.zombiezite.model.baseImpl._
import org.mongodb.scala.{ Completed, Document, MongoClient, MongoCollection, MongoDatabase, Observable, Observer, SingleObservable }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class MongoDAO() extends DAOTrait {
  val client: MongoClient = MongoClient()
  val database: MongoDatabase = client.getDatabase("ZombieZiteMongo")
  val collection: MongoCollection[Document] = database.getCollection("ZombieZiteMongoCollection")

  implicit val item = new Writes[FItemInterface] {

    def writes(i: FItemInterface): JsValue = i match {
      case w: FWeaponInterface => Json.obj(
        "name" -> w.name,
        "range" -> w.range,
        "damage" -> w.strength,
        "aoe" -> w.aoe
      )
      case a: FArmorInterface => Json.obj(
        "name" -> a.name,
        "armor" -> a.protection
      )
      case _ => Json.obj(
        "name" -> i.name
      )
    }
  }

  implicit val readWeapon: Reads[FWeapon] = new Reads[FWeapon] {
    def reads(js: JsValue): JsResult[FWeapon] = {
      JsSuccess(FWeapon((js \ "name").as[String], (js \ "str").as[Int], (js \ "range").as[Int]))
    }
  }

  implicit val readArmor: Reads[FArmor] = new Reads[FArmor] {
    def reads(js: JsValue): JsResult[FArmor] = {
      JsSuccess(FArmor((js \ "name").as[String], (js \ "armor").as[Int]))
    }
  }

  implicit val position = new Writes[PositionInterface] {
    def writes(pos: PositionInterface) = Json.obj(
      "x" -> pos.x,
      "y" -> pos.y
    )
  }

  implicit val readPosition: Reads[Position] = new Reads[Position] {
    override def reads(json: JsValue): JsResult[Position] = {
      JsSuccess(Position((json \ "x").as[Int], (json \ "y").as[Int]))
    }
  }

  // Cross ref -> ActualField to Position
  implicit val char = new Writes[FCharacterInterface] {
    def writes(c: FCharacterInterface): JsValue = c match {
      case p: FPlayerInterface => Json.obj(
        "lifePoints" -> p.lifePoints,
        "strength" -> p.strength,
        "armor" -> p.armor,
        "range" -> p.range,
        "name" -> p.name,
        "equippedWeapon" -> p.equippedWeapon,
        "inventory" -> p.equipment,
        "ActionCounter" -> p.actionCounter
      )
      case z: FZombieInterface => Json.obj(
        "lifePoints" -> z.lifePoints,
        "strength" -> z.strength,
        "range" -> z.range,
        "name" -> z.name
      )
      case _ => Json.obj(
        "name" -> c.name
      )
    }
  }

  implicit val readPlayer: Reads[FPlayer] = new Reads[FPlayer] {
    override def reads(json: JsValue): JsResult[FPlayer] = {
      JsSuccess(FPlayer(0, 0, (json \ "lifepoints").as[Int], name = (json \ "name").as[String]))
    }
  }

  implicit val readZombies: Reads[FZombie] = new Reads[FZombie] {
    override def reads(json: JsValue): JsResult[FZombie] = {
      JsSuccess(FZombie(9, 9, (json \ "lifepoints").as[Int], name = (json \ "name").as[String]))
    }
  }

  implicit val field = new Writes[FFieldInterface] {
    def writes(f: FFieldInterface) = Json.obj(
      "position" -> f.p,
      "players" -> f.players,
      "zombies" -> f.zombies,
      "chars" -> f.chars,
      "charCount" -> Json.toJson(f.chars.length)
    )
  }

  implicit val readField: Reads[FField] = new Reads[FField] {
    override def reads(json: JsValue): JsResult[FField] = {
      JsSuccess(FField((json \ "position").as[Position], players = (json \ "players").as[Vector[FPlayer]], zombies = (json \ "zombies").as[Vector[FZombie]]))
    }
  }

  implicit val area = new Writes[FAreaInterface] {
    def writes(a: FAreaInterface) = Json.obj(
      "fields" -> a.lines
    )
  }

  override def create(state: cState): Future[Unit] = {
    Future {

      var moreData = Json.toJson(state.area)
      /*val observable: Observable[Completed] = collection.insertOne(Document(JSONObject.apply(Map("area"
        -> state.area.lines.map(line => line.map(field => (field.p.x, field.p.y, field.charCount))))).toString()))*/

      val observable: Observable[Completed] = collection.insertOne(Document(moreData.toString()))

      observable.subscribe(new Observer[Completed] {
        override def onNext(result: Completed): Unit = println(s"onNext: $result")

        override def onError(e: Throwable): Unit = println(s"onError: $e")

        override def onComplete(): Unit = println("onComplete")
      })

      0
    }
  }

  override def read(): cState = {
    var waitVar = true
    var res = FArea(0, 0)
    val retState = cState()

    collection.find().first().subscribe(new Observer[Document] {
      override def onNext(result: Document): Unit = {
        //res = res.copy(lines = (Json.parse(result.toJson()) \ "fields").as[Array[Vector[FFieldInterface]]].toVector)
        (Json.parse(result.toJson()) \ "fields").as[Vector[Vector[FField]]]
      }

      override def onError(e: Throwable): Unit = println(e)

      override def onComplete(): Unit = {
        waitVar = false
      }
    })

    while (waitVar) {
      Thread.sleep(100)
    }

    retState.copy(area = res)
  }

  override def update(table: String, state: cState, id: Int): Future[Int] = {
    Future {
      0
    }
  }

  override def delete(table: String, id: Int, name: String): Future[Int] = {
    Future {
      0
    }
  }
}
