package App

import java.io.File

case class Config(
                   command: String = "",
                   files: Seq[File] = Seq(),
                   argument: String = "",
                   message: String = ""
                 )
