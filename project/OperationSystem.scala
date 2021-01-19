sealed trait OperationSystem
case object Windows extends OperationSystem
case object Linux extends OperationSystem
case object MacOs extends OperationSystem
case object UndefinedOs extends OperationSystem

object OperationSystem {
  def define: OperationSystem = System.getProperty("os.name").toLowerCase match {
    case x if x.contains("win")                                           => Windows
    case x if x.contains("nix") || x.contains("nux") || x.contains("aix") => Linux
    case x if x.contains("mac")                                           => MacOs
  }
}
