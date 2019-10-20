package Utils

import better.files._

import scala.annotation.tailrec

object CheckRepo {

  def checkRepo(path: String = ""): String ={
    val v= nearestRepo(path)
    if(v.isDefined) v.get
    else throw new Exception
  }

  @tailrec
  def nearestRepo(path: String = ""): Option[String] = {
    val dir = path.toFile
    if(dir.exists){
      val matches = dir.list(f => f.name == ".sgit", maxDepth = 1)
      if(matches.hasNext) {
        Some(dir.pathAsString)
      }
      else{
        if(dir.parent != null){
          nearestRepo(dir.parent.pathAsString)
        }
        else{
          None
        }
      }
    }
    else {
      None
    }
  }
}
