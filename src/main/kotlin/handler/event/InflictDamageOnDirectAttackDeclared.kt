package handler.event

import Matches
import domain.DamageType
import domain.DirectAttackDeclared
import domain.InflictDamage
import port.CommandBus
import port.EventBus
import port.EventHandler

class InflictDamageOnDirectAttackDeclared(eventBus: EventBus, private val commandBus: CommandBus, private val matches: Matches): EventHandler<DirectAttackDeclared> {
  
  init {
    eventBus.on(DirectAttackDeclared::class.java.simpleName) {
      consume(it.mapTo(DirectAttackDeclared::class.java))
    }
  }
  
  override fun consume(event: DirectAttackDeclared) {
    val match = matches.load(event.matchId)
  
    val damage = match.calcDamage(event.attackerId)
    commandBus.send(InflictDamage(event.matchId, event.by, damage, DamageType.Direct))
  }
  
}