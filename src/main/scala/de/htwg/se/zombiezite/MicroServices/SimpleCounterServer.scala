package de.htwg.se.zombiezite.MicroServices

import akka.actor.ActorSystem
import akka.http.javadsl.server.directives.RouteDirectives
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Route, StandardRoute }
import akka.stream.ActorMaterializer
import de.htwg.se.zombiezite.controller.FController
import de.htwg.se.zombiezite.controller.cState

import scala.concurrent.Future

class SimpleCounterServer(sc: SimpleCounter) {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val route: Route = get {
    pathSingleSlash {
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "<h1>SimpleCounter</h1>"))
    }
    path("") {
      statetoHtml
    } ~
      path("inc") {
        sc.increase()
        statetoHtml
      } ~
      path("dec") {
        sc.decrease()
        statetoHtml
      } ~
      path("user" / RemainingPath) { user =>
        sc.changeUser(user.toString())
        statetoHtml
      }
  }

  def statetoHtml: StandardRoute = {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, sc.toHtml))
  }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "0.0.0.0", 8081)

  def unbind(): Unit = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}

