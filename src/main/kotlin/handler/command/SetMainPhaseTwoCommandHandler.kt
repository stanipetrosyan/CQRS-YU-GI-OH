package handler.command

import Matches
import domain.SetMainPhaseOne
import domain.SetMainPhaseTwo
import domain.Turn
import port.CommandHandler
import port.CommandResult

class SetMainPhaseTwoCommandHandler(private val matches: Matches): CommandHandler<SetMainPhaseTwo> {
  override fun handle(command: SetMainPhaseTwo): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.changePhaseIsPermitted(Turn.State.MainPhase2))
      matches.save(match.changeTurnPhase(command.by, Turn.State.MainPhase2))
  
    return CommandResult.OK
  
  }
  
}
