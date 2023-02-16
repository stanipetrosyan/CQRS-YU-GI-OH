package handler.command

import Matches
import domain.ActiveSpell
import domain.NormalSummonMonster
import domain.Turn
import port.CommandHandler
import port.CommandResult

class ActiveSpellCommandHandler(private val matches: Matches): CommandHandler<ActiveSpell> {
  
  override fun handle(command: ActiveSpell): CommandResult {
    val match = matches.load(command.matchId)
    matches.save(match.activeSpell(command.by, command.spell))
    
    return CommandResult.OK
  }
}