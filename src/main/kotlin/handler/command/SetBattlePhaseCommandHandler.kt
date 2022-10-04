package handler.command

import Matches
import domain.*
import port.CommandHandler
import port.CommandResult

class SetBattlePhaseCommandHandler(private val matches: Matches): CommandHandler<SetBattlePhase> {
  override fun handle(command: SetBattlePhase): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.changePhaseIsPermitted(Turn.State.BattlePhase))
      matches.save(match.changeTurnPhase(command.by, Turn.State.BattlePhase))
  
    return CommandResult.OK
  
  }
  
}
