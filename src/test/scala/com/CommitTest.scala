package com

import Utils.FileManager
import org.scalatest._

class CommitTest extends FunSuite with BeforeAndAfterEach {

  override def beforeEach{
    FileManager.reset()
  }

}
