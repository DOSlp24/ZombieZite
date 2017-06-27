package de.htwg.se.zombiezite.aview

import de.htwg.se.zombiezite.controller._

class GetIcon(c: Controller) {
  var folder: String = "media"
  var path: String = ""
  var zombieList = c.getZombieList
  var itemList = c.getZombieList
  var playerList = c.playerNamer

  def execute(name: String, mode: String): String = {
    path = "/" + name + ".png"
    mode match {
      case "Item" => {
        folder = folder + "/items"
        if (!itemList.contains(name)) {
          path = "/Default.png"
        }
      }
      case "Player" => {
        folder = folder + "/players"
        if (!playerList.contains(name)) {
          path = "/Default.png"
        }
      }
      case "Weapon" => {
        folder = folder + "/weapons"
        if (!itemList.contains(name)) {
          path = "/Default.png"
        }
      }
      case "Zombie" => {
        folder = folder + "/zombies"
        if (!zombieList.contains(name)) {
          path = "/Default.png"
        }
      }
      case "Por" => {
        folder = folder + "/players"
        if (!playerList.contains(name)) {
          path = "/Default Por.png"
        } else {
          path = "/" + name + " Por.png"
        }
      }
    }
    return folder + path
  }
}