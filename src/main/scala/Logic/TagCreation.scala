package Logic

import App.Config
import Entity.Branch
import Utils.DatabaseManager

object TagCreation {

  def action: Function[Config,Int] = (config:Config) => {
    newTag(config)
  }

  def newTag(config: Config): Int = {
    val currentBranch: Branch = DatabaseManager.getCurrentBranch
    if(currentBranch.ref.isDefined){
      val newTag = Entity.Tag(name = config.argument, ref = currentBranch.ref.get)
      InputOutput.Serializer.writeObject(newTag,".sgit/refs/tags/" + config.argument)
      100
    }
    else {
      203
    }
  }
}

