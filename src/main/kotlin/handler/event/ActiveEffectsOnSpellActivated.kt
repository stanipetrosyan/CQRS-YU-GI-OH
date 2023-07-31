package handler.event

import domain.*
import port.CommandBus
import port.EventBus
import port.EventHandler

class ActiveEffectsOnSpellActivated(eventBus: EventBus, private val commandBus: CommandBus): EventHandler<SpellActivated> {
  
  init {
    eventBus.on(SpellActivated::class.java.simpleName) {
      consume(it.mapTo(SpellActivated::class.java))
    }
  }
  
  override fun consume(event: SpellActivated) {


    event.spell.effects.map { effect: Effect ->
      when (effect) {
        is DrawCards -> {
          commandBus.send(DrawCard(event.matchId, event.by))
          commandBus.send(DrawCard(event.matchId, event.by))
        }

        is DestroyCard -> commandBus.send(DestroyMonster(event.matchId, event.by, effect.cardId))

        is DiscardCards -> TODO()
      }

    }
  }
  
}