package port

import domain.MatchCommand

interface CommandBus {
  fun send(command: MatchCommand): CommandResult
}


enum class CommandResult(val value: String) {
  OK("ok"),
  MonsterCannotAttack("Monster cannot attack")
}