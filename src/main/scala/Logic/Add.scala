package Logic

import App.Config
import Entity.Index
import Utils.{DatabaseManager, FileManager}

object Add {

  def action: Function[Config,Int] = (config:Config) => {
    add(config)
  }

  final def add(config: Config): Int ={
    @scala.annotation.tailrec
    def recursiveLoop(config: Config): Unit = {
      var filesToAdd = config.files
      val file = filesToAdd.headOption
      val currentIndex = DatabaseManager.loadIndex().stagedFiles

      if(file.isEmpty){
        return
      }
      if(!file.get.exists()){
        if(currentIndex.values.toSet.contains(FileManager.getRelativePath(file.get.getAbsolutePath))){
          //deleteFile
        }
        else {
          Utils.Logger.log("File "+file.get.getPath+" does not exists")
        }
      }
      else{
        val currentIndex: Index = DatabaseManager.loadIndex()
        if(!file.get.isDirectory){
          if(currentIndex.existsInIndex(file.get)){
            currentIndex.replace(file.get)
          }
          else {
            currentIndex.addStagedFile(file.get)
          }
        }
        else {
          filesToAdd = filesToAdd ++ file.get.listFiles()
        }
      }
      recursiveLoop(config.copy(files = filesToAdd.tail))
    }

    recursiveLoop(config)
    200
  }
}
