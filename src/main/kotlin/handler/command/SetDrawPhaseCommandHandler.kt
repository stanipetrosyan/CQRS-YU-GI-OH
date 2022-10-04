package handler.command

import Matches
import domain.SetDrawPhase
import domain.SetMainPhaseOne
import domain.Turn
import port.CommandHandler
import port.CommandResult

class SetDrawPhaseCommandHandler(private val matches: Matches): CommandHandler<SetDrawPhase> {
  override fun handle(command: SetDrawPhase): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.changePhaseIsPermitted(Turn.State.DrawPhase))
      matches.save(match.changeTurnPhase(command.by, Turn.State.DrawPhase))
  
    return CommandResult.OK
  
  }
  
}
