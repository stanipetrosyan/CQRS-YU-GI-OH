package handler.command

import Matches
import domain.InflictDamage
import port.CommandHandler
import port.CommandResult

class InflictDamageCommandHandler(private val matches: Matches) : CommandHandler<InflictDamage> {
  
  override fun handle(command: InflictDamage): CommandResult {
    val match = matches.load(command.matchId)
    
    matches.save(match.inflictDamage(command.by, command.damage, command.type))
    return CommandResult.OK
  
  }
}