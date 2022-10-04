package port

import domain.Command

interface CommandHandler<T: Command> {
  fun handle(command: T): CommandResult
}