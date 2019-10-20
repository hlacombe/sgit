package App
import java.io.File

import scopt.{OParser, OParserBuilder}

object Parser extends App {

  val builder: OParserBuilder[Config] = OParser.builder[Config]
  val parser1: OParser[Unit, Config] = {
    import builder._
    OParser.sequence(
      programName("scopt"),
      head("scopt", "1.0"),
      cmd("init")
        .action((_, c) => c.copy(command = "init"))
        .text("initialize the sgit directory"),
      cmd(name = "status")
        .action((_, c) => c.copy(command = "status"))
        .text("get the status of the current branch vs the working tree"),
      cmd(name = "add")
        .action((_, c) => c.copy(command = "add"))
        .text("Add <files> to the current index")
        .children(
          arg[File]("<file>...")
            .unbounded()
            .optional()
            .action((x, c) => c.copy(files = c.files :+ x))
            .text("optional unbounded args")
        ),
      cmd(name = "reset")
        .action((_, c) => c.copy(command = "reset")),
      cmd(name = "log")
        .action((_, c) => c.copy(command = "log")),
      cmd(name = "commit")
        .action((_, c) => c.copy(command = "commit"))
        .text("Commit the current index")
        .children(
          arg[String]("message")
            .action((x, c) => c.copy(message = x))
            .text("The Commit message")
        ),
      cmd(name = "branch")
        .action((_, c) => c.copy(command = "branch"))
        .text("Create a new Branch")
        .children(
          opt[Unit]("av")
            .optional()
            .action((_, c) => c.copy(command = "branch -av"))
            .text("list branches and tags"),
          arg[String]("<name>")
            .optional()
            .action((x, c) => c.copy(argument = x))
            .text("Branch name")
        ),
      cmd(name = "tag")
        .action((_, c) => c.copy(command = "tag"))
        .text("Create a new Tag")
        .children(
          arg[String]("tag name")
            .action((x, c) => c.copy(argument = x))
            .text("tag name")
        ),
        cmd(name = "checkout")
        .action((_, c) => c.copy(command = "checkout"))
        .text("Checkout from the provided reference")
        .children(
          arg[String]("Reference")
            .action((x, c) => c.copy(argument = x))
        )
    )
  }

  // OParser.parse returns Option[Config]

    OParser.parse(parser1, args, Config()) match {
      case Some(config) =>
        Dispatcher.Dispatch(config)
      case _ =>
        // arguments are bad, error message will have been displayed
        println("Parser Error")
    }

}
