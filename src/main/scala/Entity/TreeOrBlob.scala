package Entity

abstract class TreeOrBlob(hash: String, path: String) {

  override def toString: String = {
    "hash " + hash + " path " + path
  }

  def get_path(): String ={
    path
  }

  def get_hash(): String ={
    hash
  }

}
