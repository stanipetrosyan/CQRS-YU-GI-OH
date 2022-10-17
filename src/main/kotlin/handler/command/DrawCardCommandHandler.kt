package handler.command

import domain.DrawCard
import Matches
import port.CommandHandler
import port.CommandResult

class DrawCardCommandHandler(private val matches: Matches): CommandHandler<DrawCard> {
  override fun handle(command: DrawCard): CommandResult {
    val match = matches.load(command.matchId)
    
    matches.save(match.drawCard(command.by))
    return CommandResult.OK
  }
  
}
