package Entity

import java.io.File

import Utils.FileManager

import scala.collection.immutable.HashMap

case class Index(
                stagedFiles: Map[String, String] = HashMap() //(Hash,Path)
                ) extends Serializable {

  def replace(get: File): Unit = {
    val infos = Utils.FileManager.getInfos(get)
    val hash = stagedFiles.filter(t => t._2 == infos._2).head
    val newIndex = Index(stagedFiles = stagedFiles.filter(t => t._2 != infos._2))
    FileManager.deleteObject(hash._1)
    newIndex.addStagedFile(get)
  }

  def addStagedFile(file: File) {
    val infos = Utils.FileManager.getInfos(file)
    Utils.FileManager.copyToDatabase(from = infos._2, withName = infos._1)
    val newIndex = Index(stagedFiles = stagedFiles + (infos._1 -> infos._2))
    InputOutput.Serializer.writeObject(newIndex,".sgit/index")
  }

  def existsInIndex(file: File): Boolean = {
    val infos = Utils.FileManager.getInfos(file)
    stagedFiles.get(infos._1).isDefined || stagedFiles.values.toArray.contains(infos._2)
  }

  def delete(get: File): Unit = {
    val infos = Utils.FileManager.getInfos(get)
    val path = stagedFiles.find(t => t._2 == infos._2)
    if(path.isDefined){
      val newIndex = Index(stagedFiles = stagedFiles.filter( t => t._2 != infos._2))
      InputOutput.Serializer.writeObject(newIndex,".sgit/index")
    }
  }

}


