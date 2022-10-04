package handler.command

import Matches
import domain.SetDrawPhase
import domain.SetMainPhaseOne
import domain.SetStandByPhase
import domain.Turn
import port.CommandHandler
import port.CommandResult

class SetStandByPhaseCommandHandler(private val matches: Matches): CommandHandler<SetStandByPhase> {
  override fun handle(command: SetStandByPhase): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.changePhaseIsPermitted(Turn.State.StandByPhase)) {
      matches.save(match.changeTurnPhase(command.by, Turn.State.StandByPhase))
    }
    return CommandResult.OK
  
  }
  
}
