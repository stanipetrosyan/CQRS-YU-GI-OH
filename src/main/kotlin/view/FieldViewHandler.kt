package view

import domain.*
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject

class FieldViewHandler(eventBus: EventBus) {
  
  private var deckSize: Int = 0
  
  init {
    eventBus.consumer<JsonObject>(MatchStarted::class.simpleName) {
      consume(it.body().mapTo(MatchStarted::class.java))
    }
    
    
    eventBus.consumer<JsonObject>(CardDrew::class.simpleName) {
      consume(it.body().mapTo(CardDrew::class.java))
    }
    
    eventBus.consumer<JsonObject>(MonsterNormalSummoned::class.simpleName) {
      consume(it.body().mapTo(MonsterNormalSummoned::class.java))
    }
    
  }

  private fun consume(event: MatchStarted) {
    deckSize = event.player.deckSize
    println("incontro iniziato con il player ${event.player.username}")
  }
  
  private fun consume(event: CardDrew) {
    deckSize -= 1
    println("player ${event.by} ha pescato una carta rimangono ${deckSize} carte")
  }
  
  private fun consume(event: MonsterNormalSummoned) {
    println("ho evocato il mostro: ${event.monster.name}")
  }
  
}