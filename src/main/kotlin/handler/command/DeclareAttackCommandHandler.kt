package handler.command

import Matches
import domain.DeclareAttack
import domain.Turn
import port.CommandHandler
import port.CommandResult

class DeclareAttackCommandHandler(private val matches: Matches): CommandHandler<DeclareAttack> {
  
  override fun handle(command: DeclareAttack): CommandResult {
    val match = matches.load(command.matchId)
    
    return when {
      !match.isPhase(Turn.State.BattlePhase) -> CommandResult.MonsterCannotAttack
      !match.monsterCanAttack(command.attacker) -> CommandResult.MonsterCannotAttack
      else -> {
        matches.save(match.declareAttack(command.by, command.attacker, command.defender))
        CommandResult.OK
      }
    }
  }
}