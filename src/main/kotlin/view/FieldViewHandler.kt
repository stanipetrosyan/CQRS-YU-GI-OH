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
    
    eventBus.consumer<JsonObject>(MonsterDestroyed::class.simpleName) {
      consume(it.body().mapTo(MonsterDestroyed::class.java))
    }
    
    eventBus.consumer<JsonObject>(MatchEnded::class.simpleName) {
      consume(it.body().mapTo(MatchEnded::class.java))
    }
    
  }
  
  private fun consume(event: MatchEnded) {
    println("Player ${event.winner} won")
  }
  
  private fun consume(event: MonsterDestroyed) {
    println("Monster Destroyed")
  }
  
  private fun consume(event: MatchStarted) {
    deckSize = event.player.deckSize
    println("Player ${event.player.username} start the match")
  }
  
  private fun consume(event: CardDrew) {
    deckSize -= 1
    println("player ${event.by} has drawn a card. Cards remaining: $deckSize")
  }
  
  private fun consume(event: MonsterNormalSummoned) {
    println("Player: ${event.by} summoned monster: ${event.monster.name}")
  }
  
}