package Logic

import App.Config
import Entity.Branch
import Utils.DatabaseManager

object BranchCreation {

  def action: Function[Config,Int] = (config:Config) => {
    newBranch(config)
  }

  def newBranch(config: Config): Int ={
    val currentBranch: Branch = DatabaseManager.getCurrentBranch
    if(currentBranch.ref.isEmpty){
      202
    }
    else{
      val newBranch = Entity.Branch(name = config.argument, ref = currentBranch.ref)
      InputOutput.Serializer.writeObject(newBranch,".sgit/refs/head/" + config.argument)
      100
    }
  }
}
