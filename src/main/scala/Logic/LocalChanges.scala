package Logic

object LocalChanges {

  def getUntrackedFiles(wdFilesPath: Set[String], allObjects: Set[String]): Set[String] = {
    wdFilesPath.diff(allObjects)
  }

  def getStagedFiles(indexFiles: Map[String, String], lastCommit: Map[String, String]): Set[String] = {
    indexFiles.values.toSet.diff(lastCommit.values.toSet)
  }

  def getModifiedFiles(indexFiles: Map[String, String], lastCommit: Map[String, String]): Map[String, String] = {
    indexFiles.filter (p => {
      indexFiles.values.toSet.diff(lastCommit.values.toSet).contains(p._2) &&
        !indexFiles.keys.toSet.diff(lastCommit.keys.toSet).contains(p._1)
    })
  }

  def getDeletedFiles(indexFiles: Map[String, String], wdFilesPath: Set[String]): Set[String] = {
    indexFiles.values.toSet.diff(wdFilesPath)
  }

}
