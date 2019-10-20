package Logic

import App.Config
import Entity.Branch
import Utils.{DatabaseManager, Logger}

object Log {

  def action: Function[Config,Int] = (config:Config) => {
    log(config)
  }

  def log(config: Config): Int ={
    val allCommits: Branch = DatabaseManager.getCurrentBranch
    Logger.log("On branch "+allCommits.name)
    var currentCommit = allCommits.ref
    while(currentCommit.isDefined){
      Logger.log(currentCommit.get.hash + " " + currentCommit.get.date)
      Logger.log(currentCommit.get.comment)
      currentCommit = currentCommit.get.parent
    }
    200
  }
}
