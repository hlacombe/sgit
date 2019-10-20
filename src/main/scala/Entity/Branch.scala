package Entity

case class Branch(name: String,
                  ref: Option[Commit])
