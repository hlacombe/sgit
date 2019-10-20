package App
import Logic._
import Utils.{CheckRepo, FileManager, Logger}

object Dispatcher{
  def Dispatch(config: Config): Unit = {
    if(config.command != "init" && CheckRepo.nearestRepo().isEmpty){
      Logger.log("Not a Sgit repository")
    }
    else {
      var toExecute: Function[Config,Int] = { _ => 0}
      config.command match{
        case "init" => toExecute = Create.action
        case "status" => toExecute = Status.action
        case "add" => toExecute = Add.action
        case "commit" => toExecute = CommitAction.action
        case "log" => toExecute = Log.action
        case "branch" => toExecute = BranchCreation.action
        case "tag" => toExecute = TagCreation.action
        case "branch -av" => toExecute = ListReferences.action
        case "checkout" => toExecute = Checkout.action

        case "reset" => FileManager.reset()
        case _ => println("Dispatcher Error")
      }
      val result = toExecute.apply(config)
      Logger.log(Responses.getResponseMessage(result))
    }
  }
}

