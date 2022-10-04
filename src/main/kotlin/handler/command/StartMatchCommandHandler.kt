package handler.command

import domain.Match
import Matches
import domain.StartMatch
import port.CommandHandler
import port.CommandResult

class StartMatchCommandHandler(private val matches: Matches): CommandHandler<StartMatch> {
  override fun handle(command: StartMatch): CommandResult {
    matches.save(Match(command.matchId).start(command.player, command.opponent))
    return CommandResult.OK
  }
  
}
