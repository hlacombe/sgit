package Logic

import java.time.Instant

import App.Config
import Entity.Commit
import InputOutput.Serializer
import Utils.{CheckRepo, DatabaseManager, FileManager}

object CommitAction {

  def action: Function[Config,Int] = (config:Config) => {
    commit(config)
  }

  def commit(config: Config): Int ={
    val message = config.message
    val index = DatabaseManager.loadIndex()
    val currentBranch = DatabaseManager.getCurrentBranch

    if(currentBranch.ref.isDefined && index.stagedFiles.toSet.diff(currentBranch.ref.get.stagedFiles.toSet).isEmpty){
      201
    }
    else {
      val newCommit = Commit(
        hash = "", comment =
          message, parent = currentBranch.ref, date = Instant.now, stagedFiles = index.stagedFiles
      )

      Serializer.writeObject(newCommit, path = CheckRepo.nearestRepo().get + "/.sgit/objects/commits/tmp")
      val sha1 = FileManager.checksum(CheckRepo.nearestRepo().get + "/.sgit/objects/commits/tmp")
      val finalCommit = newCommit.copy(hash = sha1)
      Serializer.writeObject(finalCommit, path = CheckRepo.nearestRepo().get + "/.sgit/objects/commits/tmp")
      FileManager.rename(CheckRepo.nearestRepo().get + "/.sgit/objects/commits/tmp", sha1)

      if(!DatabaseManager.isHeadDetached){
        val newBranch = currentBranch.copy(ref = Some(finalCommit))
        InputOutput.Serializer.writeObject(newBranch,".sgit/refs/head/" + newBranch.name)
      }
      102
    }
  }
}
