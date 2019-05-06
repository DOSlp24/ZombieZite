package de.htwg.se.zombiezite.aview.api

import akka.actor.ActorSystem
import akka.http.javadsl.server.directives.RouteDirectives
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.stream.ActorMaterializer
import de.htwg.se.zombiezite.controller.FController
import de.htwg.se.zombiezite.controller.cState

import scala.concurrent.Future

class HttpServer(controller: FController) {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  var state: cState = cState()

  val route: Route = get {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>Zombiezite</h1>"))
    }
    path("zombiezite") {
      statetoHtml
    } ~
      path("zombiezite" / "init") {
        this.state = controller.init()
        statetoHtml
      } ~
      path("zombiezite" / "new") {
        // new = alias for init.
        this.state = controller.init()
        statetoHtml
      } ~
      path("zombiezite" / "moveDown") {
        this.state = controller.move(this.state, "down")
        statetoHtml
      } ~
      /*
      path("zombiezite" / "move" / Directio) { command => {
      // TODO: does not work yet...
        controller.move(controller.init(), Directio)
        statetoHtml
      }
      }~
      */
      path("zombiezite" / "wait") {
        this.state = controller.wait(this.state)
        statetoHtml
      }
    // TODO: more commands
  }

  def statetoHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>HTWG Sudoku</h1>" + controller.stateToHtml(this.state)))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
