package Logic

import App.Config
import Utils.{DatabaseManager, FileManager, Logger}

import scala.collection.immutable.HashMap

object Status {

  def action: Function[Config,Int] = (config:Config) => {
    status(config)
  }

  def status(config: Config): Int ={

    val currentIndex = DatabaseManager.loadIndex()
    var lastCommit: Map[String,String] = new HashMap()
    if(DatabaseManager.getCurrentBranch.ref.isDefined){
      lastCommit = DatabaseManager.getCurrentBranch.ref.get.stagedFiles
    }
    val indexFiles = currentIndex.stagedFiles
    val allObjects = Utils.DatabaseManager.getAllHash.toSet
    val wdFilesPath = FileManager.getFlatWorkingTreeFiles(FileManager.walkWorkingTree()).map(t => t._2).toSet

    Logger.log("Untracked files")
    val untracked = LocalChanges.getUntrackedFiles(wdFilesPath, allObjects)
    untracked.foreach(t => Logger.log(t))

    Logger.log("Staged Files")
    val newStaged = LocalChanges.getStagedFiles(indexFiles, lastCommit)
    newStaged.foreach(t => Logger.log(t))

    Logger.log("Modified Files")

    Logger.log("Deleted Files")
    val deleted = LocalChanges.getDeletedFiles(indexFiles,wdFilesPath)
    deleted.foreach(t => Logger.log(t))

    103
  }
}
