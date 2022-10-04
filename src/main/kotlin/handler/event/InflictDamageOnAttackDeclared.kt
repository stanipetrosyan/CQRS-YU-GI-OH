package handler.event

import Matches
import domain.AttackDeclared
import domain.InflictDamage
import port.CommandBus
import port.EventBus
import port.EventHandler

class InflictDamageOnAttackDeclared(eventBus: EventBus, private val commandBus: CommandBus, private val matches: Matches): EventHandler<AttackDeclared> {
  
  init {
    eventBus.on(AttackDeclared::class.java.simpleName) {
      consume(it.mapTo(AttackDeclared::class.java))
    }
  }
  
  override fun consume(event: AttackDeclared) {
    val match = matches.load(event.matchId)
    
    val damage = match.calcDamage(event.attackerId, event.defenderId)
    val opponent = match.findOpponent(event.by)
    commandBus.send(InflictDamage(event.matchId, opponent, damage))
  }
  
}