package InputOutput
import better.files._

object Serializer {

  // Write
  def writeObject(myObject: Serializable, path: String): Unit ={
    val file = path.toFile
    file.writeSerialized(myObject)
  }

  //Read
  def readObject[A](path: String): A={
    val file = path.toFile
    file.readDeserialized[A]()
  }

}
