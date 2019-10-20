package com

import App.Config
import Logic._
import Utils.FileManager
import better.files._
import org.scalatest.{FlatSpec, Matchers}
class IOTests extends FlatSpec with Matchers {

  val config = new Config()

  def beforeEach{
    FileManager.reset()
    Create.createBaseTreeStructure()
  }

  "The createFileFromFile method" should "create a file corresponding to an object stored in database" in {
    val file = "test".toFile.createIfNotExists().write("some text")
  }


}

