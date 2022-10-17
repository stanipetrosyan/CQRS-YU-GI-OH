package view

import domain.*
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject

class LifePointsViewHandler(eventBus: EventBus) {
  private var players = mutableMapOf<String, Int>()
  
  init {
    eventBus.consumer<JsonObject>(MatchStarted::class.simpleName) {
      consume(it.body().mapTo(MatchStarted::class.java))
    }
    
    eventBus.consumer<JsonObject>(BattleDamageInflicted::class.simpleName) {
      consume(it.body().mapTo(BattleDamageInflicted::class.java))
    }
    
    eventBus.consumer<JsonObject>(DirectDamageInflicted::class.simpleName) {
      consume(it.body().mapTo(DirectDamageInflicted::class.java))
    }
  }
  
  private fun consume(event: MatchStarted) {
    players[event.player.username] = event.player.lifePoints
    players[event.opponent.username] = event.opponent.lifePoints
    println("Giocatori con 8000 life points ciascuno")
  }
  
  private fun consume(event: BattleDamageInflicted) {
    players[event.by] = players[event.by]!! - event.damage
    println("Il giocatore ${event.by} ha subito ${event.damage} in battaglia e i suoi life point sono ${players[event.by]}")
  }
  
  private fun consume(event: DirectDamageInflicted) {
    players[event.by] = players[event.by]!! - event.damage
    println("Il giocatore ${event.by} ha subito ${event.damage} come attacco diretto e i suoi life point sono ${players[event.by]}")
  }
 
}