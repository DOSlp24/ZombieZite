package de.htwg.se.zombiezite.MicroServices

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Route, StandardRoute }
import akka.stream.ActorMaterializer
import de.htwg.se.zombiezite.controller.FController

import scala.concurrent.Future

class SimulationServer(c: FController) {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  var state = "Ok"
  var cState = c.init()

  val route: Route = get {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Item Deck</h1>"))
    }
    path("") {
      cState = c.init()
      statetoHtml
    } ~
      path("move" / RemainingPath) { direction =>
        cState = c.move(cState, direction.toString())
        statetoHtml
      } ~
      path("search") {
        cState = c.search(cState)
        statetoHtml
      } ~
      path("wait") {
        cState = c.wait(cState)
        statetoHtml
      }
  }

  def statetoHtml: StandardRoute = {
    if (cState.player.size <= 0) {
      this.state = "Game Over"
    }
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>" + state + "</h1>"))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "0.0.0.0", 8002)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
