package Utils
import Entity.{Branch, Commit, Index, Tag}
import better.files._

object DatabaseManager {
  def getFileByHash(hash: String): File = {
    (CheckRepo.nearestRepo().get+"/.sgit/objects/"+hash).toFile
  }

  def getAllTags: Seq[Tag] = {
    val files = (CheckRepo.nearestRepo().getOrElse("") + "/.sgit/refs/tags").toFile.listRecursively
    files.map( f => InputOutput.Serializer.readObject(f.pathAsString).asInstanceOf[Tag]).toSeq
  }

  def getAllBranches: Seq[Branch] = {
    val files = (CheckRepo.nearestRepo().getOrElse("") + "/.sgit/refs/head").toFile.listRecursively
    files.map( f => InputOutput.Serializer.readObject(f.pathAsString).asInstanceOf[Branch]).toSeq
  }

  def getCurrentBranch: Branch = {
    val refToBranch = (CheckRepo.nearestRepo().getOrElse("") + "/.sgit/HEAD").toFile.lineIterator.next()
    if(refToBranch == "detached head"){
      val commit = InputOutput.Serializer.readObject(CheckRepo.nearestRepo().getOrElse("") + "/.sgit/HEAD-DETACHED").asInstanceOf[Commit]
      val fakeBranch = Branch(name = "detached head" , ref = Some(commit))
      println("fakeBranch: " + fakeBranch)
      fakeBranch
    }
    else {
      println(refToBranch)
      val file = refToBranch.toFile
      if(file.exists){
        InputOutput.Serializer.readObject(CheckRepo.nearestRepo().getOrElse("") +"/"+ refToBranch).asInstanceOf[Branch]
      } else {
        val newBranch = Entity.Branch(name = "master", ref = None)
        InputOutput.Serializer.writeObject(newBranch,".sgit/refs/head/master")
        println("NewBranch: " + newBranch)
        newBranch
      }
    }
  }

  def getAllHash: Seq[String] = {
    (CheckRepo.nearestRepo().get+"/.sgit/objects").toFile.list(_ => true).filterNot(f => f.isDirectory).map(t => t.name).toSeq
  }

  def getAllCommits: Seq[Commit] ={
    val files = (CheckRepo.nearestRepo().getOrElse("") + "/.sgit/objects/commits").toFile.listRecursively
    files.map( f => InputOutput.Serializer.readObject(f.pathAsString).asInstanceOf[Commit]).toSeq
  }

  def loadIndex(): Index ={
    InputOutput.Serializer.readObject(CheckRepo.nearestRepo().getOrElse("")+"/.sgit/index")
  }

  def getBranchByName(name: String): Branch ={
    InputOutput.Serializer.readObject(CheckRepo.nearestRepo().getOrElse("") + "/.sgit/refs/head/"+name).asInstanceOf[Branch]
  }

  def getTagByName(name: String): Tag ={
    InputOutput.Serializer.readObject(CheckRepo.nearestRepo().getOrElse("") + "/.sgit/refs/tags/"+name).asInstanceOf[Tag]
  }

  def getCommitByHash(name: String): Commit ={
    InputOutput.Serializer.readObject(CheckRepo.nearestRepo().getOrElse("") + "/.sgit/objects/commits/"+name).asInstanceOf[Commit]
  }

  def getCheckoutCommit: Commit ={
    InputOutput.Serializer.readObject(CheckRepo.nearestRepo().getOrElse("") + "/.sgit/HEAD-DETACHED").asInstanceOf[Commit]
  }

  def isHeadDetached: Boolean = {
    (CheckRepo.nearestRepo().getOrElse("") + "/.sgit/HEAD").toFile.lineIterator.next() == "detached head"
  }
}
