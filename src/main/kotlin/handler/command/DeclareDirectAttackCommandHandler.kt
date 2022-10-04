package handler.command

import Matches
import domain.DeclareDirectAttack
import domain.Turn
import port.CommandHandler
import port.CommandResult

class DeclareDirectAttackCommandHandler(private val matches: Matches): CommandHandler<DeclareDirectAttack> {
  
  override fun handle(command: DeclareDirectAttack): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.isPhase(Turn.State.BattlePhase) && match.monsterCanAttack(command.monsterId)) {
      matches.save(match.declareDirectAttack(command.to, command.monsterId))
    }
    return CommandResult.OK
  
  }
}