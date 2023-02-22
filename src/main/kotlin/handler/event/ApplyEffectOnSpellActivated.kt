package handler.event

import domain.*
import port.CommandBus
import port.EventBus
import port.EventHandler

class ApplyEffectOnSpellActivated(eventBus: EventBus, private val commandBus: CommandBus): EventHandler<SpellActivated> {
  
  init {
    eventBus.on(SpellActivated::class.java.simpleName) {
      consume(it.mapTo(SpellActivated::class.java))
    }
  }
  
  override fun consume(event: SpellActivated) {
    when (event.spell.effect.type) {
      is DrawCards -> {
        commandBus.send(DrawCard(event.matchId, event.by))
        commandBus.send(DrawCard(event.matchId, event.by))
      }
    }
  }
  
}