package Entity

import java.time.Instant

import scala.collection.immutable.HashMap

case class Commit(
                 hash: String,
                 comment: String,
                 parent: Option[Commit] = None,
                 date: Instant,
                 stagedFiles: Map[String, String] = HashMap()
                 )
