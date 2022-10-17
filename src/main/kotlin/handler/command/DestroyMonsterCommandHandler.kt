package handler.command

import Matches
import domain.DestroyMonster
import port.CommandHandler
import port.CommandResult

class DestroyMonsterCommandHandler(private val matches: Matches): CommandHandler<DestroyMonster> {
  override fun handle(command: DestroyMonster): CommandResult {
    val match = matches.load(command.matchId)
    
    matches.save(match.destroyMonster(command.by, command.monsterId))
    return CommandResult.OK
  }
  
}
