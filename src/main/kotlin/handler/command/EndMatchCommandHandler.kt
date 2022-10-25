package handler.command

import Matches
import domain.EndMatch
import domain.Match
import port.CommandHandler
import port.CommandResult

class EndMatchCommandHandler(private val matches: Matches): CommandHandler<EndMatch> {
  override fun handle(command: EndMatch): CommandResult {
    val match = matches.load(command.matchId)
    
    matches.save(match.end(command.by))
    return CommandResult.OK
  }
  
}
