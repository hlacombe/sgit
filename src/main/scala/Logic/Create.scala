package Logic
import App.Config
import Utils.FileManager

object Create{

  def action: Function[Config,Int] = (_:Config) => {
    createBaseTreeStructure()
  }

  def createBaseTreeStructure(): Int ={
    val structure = (".sgit",true)::
      (".sgit/objects",true)::
      (".sgit/refs",true)::
      (".sgit/refs/head",true)::
      (".sgit/refs/tags",true)::
      (".sgit/HEAD",false)::
      (".sgit/index",false)::
      (".sgit/HEAD-DETACHED",false)::
      (".sgit/objects/commits",true)::
      Nil
    FileManager.createFiles1(structure)
    FileManager.writeFile(".sgit/HEAD", ".sgit/refs/head/master")
    val index = Entity.Index()
    InputOutput.Serializer.writeObject(index, ".sgit/index")
    101
  }
}
