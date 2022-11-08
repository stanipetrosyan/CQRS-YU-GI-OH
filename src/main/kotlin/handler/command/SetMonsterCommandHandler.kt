package handler.command

import Matches
import domain.SetMonster
import domain.Turn
import port.CommandHandler
import port.CommandResult

class SetMonsterCommandHandler(private val matches: Matches): CommandHandler<SetMonster> {
  
  override fun handle(command: SetMonster): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.isPermittedNormalSummon() && match.isPhase(Turn.State.MainPhase1)) {
      matches.save(match.setMonster(command.by, command.monster))
    }
    return CommandResult.OK
  
  }
}