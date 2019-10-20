package Utils
import java.io.{File => JFile}

import better.files._

object FileManager {

  def createFileFromFile(path: String, hash: String): Option[File] = {
    val content = DatabaseManager.getFileByHash(hash)
    if(content.exists){
      Some(createFile(path).overwrite(content.contentAsString))
    }
    else{
      None
    }
  }

  def deleteFiles(values: Iterable[String]): Unit = {
    values.foreach(f => deleteFile(f))
  }

  def rename(str: String, sha1: String): Unit = {
    str.toFile.renameTo(sha1)
  }

  def deleteObject(hash: String): Unit = {
    deleteFile(".sgit/objects/"+hash)
  }

  def deleteFile(path: String): Unit ={
    val f = path.toFile
    if(f.exists)
      path.toFile.delete()
  }

  def reset(): Unit = {
    val file = ".sgit".toFile
    file.delete()
  }

  def getRelativePath(path: String): String ={
    path.toFile.relativize(CheckRepo.checkRepo().toFile).toString
  }

  def copyToDatabase(from: String, withName: String): File = {
    from.toFile.copyTo((CheckRepo.checkRepo(from)+"/.sgit/objects/"+withName).toFile).createIfNotExists(createParents = true)
  }

  def createFile(path: String, isDir: Boolean = false): File ={
    File(path)
      .createIfNotExists(isDir)
  }

  def createDirectory(path: String): Unit ={
    createFile(path, isDir = true)
  }

  @scala.annotation.tailrec
  def createFiles1(paths: Seq[(String,Boolean)]): Unit ={
    if(paths.nonEmpty){
      createFile(paths.head._1,paths.head._2)
      createFiles1(paths.tail)
    }
  }

  def walkWorkingTree(): Iterator[File] ={
    val dir = CheckRepo.nearestRepo().getOrElse("").toFile
    val matches: Iterator[File] = dir.walk().filterNot(f => f.path.toString.contains(".sgit") || f.path.toString.contains(".git"))
    matches
  }

  def checksum(file: File): String ={
    file.sha1
  }

  def checksum(file: String): String ={
    file.toFile.sha1
  }

  def writeFile(path: String, string: String): Unit ={
    val file = path.toFile
    file.write(string)
  }

  def getInfos(file: JFile): (String, String) ={
    val path = file.toPath.toString
    val hash = checksum(path.toFile)
    (hash, path)
  }

  def getInfos(file: File): (String, String) ={
    val path = CheckRepo.nearestRepo().getOrElse("").toFile.relativize(file).toString
    val hash = checksum(file)
    (hash, path)
  }

  def getFlatWorkingTreeFiles(iterator: Iterator[File]): Seq[(String, String)] ={
    var result: Seq[(String, String)] = Seq()
    iterator.foreach(f => if(!f.isDirectory) result = result:+getInfos(f))
    result
  }
}
