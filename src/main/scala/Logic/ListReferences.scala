package Logic

import App.Config
import Utils.{DatabaseManager, Logger}

object ListReferences {

  def action: Function[Config,Int] = (config:Config) => {
    listAllRefs(config)
  }

  def listAllRefs(config: Config): Int = {
    val tags = DatabaseManager.getAllTags
    val branches = DatabaseManager.getAllBranches
    branches.foreach(t => Logger.log("Branch: "+t.name))
    tags.foreach(t => Logger.log("Tag: "+t.name))
    200
  }

}
