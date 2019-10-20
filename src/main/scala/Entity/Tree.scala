package Entity

case class Tree(hash: String,
                path: String,
                var children: Option[Seq[TreeOrBlob]] = None
               ) extends TreeOrBlob(hash, path){

  def addChildren(children: Option[Seq[TreeOrBlob]]): Unit ={
    this.children = children
  }

  def get_children(): Option[Seq[TreeOrBlob]] ={
    children
  }
}
