package handler.event

import Matches
import domain.*
import port.CommandBus
import port.EventBus
import port.EventHandler

class EndMatchOnDamageInflicted(eventBus: EventBus, private val commandBus: CommandBus, private val matches: Matches): EventHandler<DamageEvent> {
  
  init {
    eventBus.on(BattleDamageInflicted::class.java.simpleName) {
      consume(it.mapTo(BattleDamageInflicted::class.java))
    }
    
    eventBus.on(DirectDamageInflicted::class.java.simpleName) {
      consume(it.mapTo(DirectDamageInflicted::class.java))
    }
    
    eventBus.on(EffectDamageInflicted::class.java.simpleName) {
      consume(it.mapTo(EffectDamageInflicted::class.java))
    }
  }
  
  override fun consume(event: DamageEvent) {
    val match = matches.load(event.matchId)
    
    if(match.playerLost(event.by, event.damage))
      commandBus.send(EndMatch(event.matchId, match.findOpponent(event.by)))
  }
  
}