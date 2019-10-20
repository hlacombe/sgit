package App

object Responses {
  def getResponseMessage(result: Int): String = responses.getOrElse(result, "An unexpected error happened")

  val responses: Map[Int, String] = Map(
    (100,"Success"),
    (200,"Failure"),
    (102,"Commit successful"),
    (103,"Status successful"),
    (201,"Nothing to commit - Aborting"),
    (202,"Can't create branch with no commits"),
    (203,"Tag not created - no commits yet"),
    (204,"Repository is not clean - Commit changes before switching branches"),
    (205,"Cannot find element to checkout from"),
    (206,"Error - No commit found for checkout")

  )

}
