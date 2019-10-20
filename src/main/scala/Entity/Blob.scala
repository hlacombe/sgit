package Entity

case class Blob(
               path: String,
               hash: String
               ) extends TreeOrBlob(hash, path){

}

