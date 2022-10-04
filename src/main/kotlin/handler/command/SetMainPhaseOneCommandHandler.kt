package handler.command

import Matches
import domain.SetMainPhaseOne
import domain.Turn
import port.CommandHandler
import port.CommandResult

class SetMainPhaseOneCommandHandler(private val matches: Matches): CommandHandler<SetMainPhaseOne> {
  override fun handle(command: SetMainPhaseOne): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.changePhaseIsPermitted(Turn.State.MainPhase1))
      matches.save(match.changeTurnPhase(command.by, Turn.State.MainPhase1))
  
    return CommandResult.OK
  
  }
  
}
