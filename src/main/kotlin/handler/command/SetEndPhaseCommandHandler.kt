package handler.command

import Matches
import domain.*
import port.CommandHandler
import port.CommandResult

class SetEndPhaseCommandHandler(private val matches: Matches): CommandHandler<SetEndPhase> {
  override fun handle(command: SetEndPhase): CommandResult {
    val match = matches.load(command.matchId)
    
    if (match.changePhaseIsPermitted(Turn.State.EndPhase))
      matches.save(match.changeTurnPhase(command.by, Turn.State.EndPhase))
  
    return CommandResult.OK
  
  }
  
}
