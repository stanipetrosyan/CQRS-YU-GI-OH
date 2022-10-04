package handler.command

import Matches
import domain.DeclareAttack
import domain.InflictDamage
import domain.Turn
import port.CommandHandler
import port.CommandResult

class InflictDamageCommandHandler(private val matches: Matches) : CommandHandler<InflictDamage> {
  
  override fun handle(command: InflictDamage): CommandResult {
    val match = matches.load(command.matchId)
    
    matches.save(match.inflictDamage(command.by, command.damage))
    return CommandResult.OK
  
  }
}