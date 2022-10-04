package handler.command

import Matches
import domain.NormalSummonMonster
import domain.Turn
import port.CommandHandler
import port.CommandResult

class NormalSummonMonsterCommandHandler(private val matches: Matches): CommandHandler<NormalSummonMonster> {
  
  override fun handle(command: NormalSummonMonster): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.isPermittedNormalSummon() && match.isPhase(Turn.State.MainPhase1)) {
      matches.save(match.summonMonster(command.monster))
    }
    return CommandResult.OK
  
  }
}