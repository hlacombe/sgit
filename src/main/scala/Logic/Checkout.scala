package Logic

import App.Config
import Entity.{Branch, Commit}
import Utils.{CheckRepo, DatabaseManager, FileManager}

object Checkout {

  def action: Function[Config,Int] = (config:Config) => {
    checkout(config)
  }

  def checkout(config: Config): Int = {
    if(!isRepoClean){
      204
    }
    else {
      val allBranches = DatabaseManager.getAllBranches.map(b => b.name)
      val allTags = DatabaseManager.getAllTags.map(t => t.name)
      val allCommits = DatabaseManager.getAllCommits.map(c => c.hash)
      val configReference = config.argument

      var target: Option[Commit] = None
      var from: String = ""
      var targetBranch: Option[Branch] = None

      configReference match {
        case x if allBranches.contains(x) =>
          target = DatabaseManager.getBranchByName(x).ref
          from = "branch"
          targetBranch = Option(DatabaseManager.getBranchByName(x))
        case x if allTags.contains(x) =>
          target = Option(DatabaseManager.getTagByName(x).ref)
          from = "tag"
        case x if allCommits.contains(x) =>
          target = Option(DatabaseManager.getCommitByHash(x))
          from = "commit"
        case _ => return 205
      }

      if(target.isEmpty){
        206
      }
      else {
        DatabaseManager.loadIndex().stagedFiles.values.foreach(t => println(t))
        target.get.stagedFiles.foreach(t => println(t))
        FileManager.deleteFiles(DatabaseManager.loadIndex().stagedFiles.values)
        //ECRIS DEDANS IDIOT
        target.get.stagedFiles.map( f => {
          FileManager.createFileFromFile(f._2, f._1)
        })

        if(from == "tag" || from == "commit") {
          FileManager.writeFile(CheckRepo.nearestRepo().getOrElse("") + "/.sgit/HEAD", "detached head")
          InputOutput.Serializer.writeObject(target, CheckRepo.nearestRepo().getOrElse("") + "/.sgit/HEAD-DETACHED")
        }
        else{
          FileManager.writeFile(CheckRepo.nearestRepo().getOrElse("") + "/.sgit/HEAD",".sgit/refs/head/"+targetBranch.get.name)
        }
        200
      }
    }

  }

  def isRepoClean: Boolean = {
    val currentBranch = DatabaseManager.getCurrentBranch
    currentBranch.ref.isDefined && currentBranch.ref.get.stagedFiles.toSet.diff(DatabaseManager.loadIndex().stagedFiles.toSet).isEmpty
  }

}
