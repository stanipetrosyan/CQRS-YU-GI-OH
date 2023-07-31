package view

import domain.*
import port.EventBus

class FieldViewHandler(eventBus: EventBus) {
  
  private var decksCount: MutableMap<String, Int> = mutableMapOf()
  
  init {
    eventBus.on(MatchStarted::class.java.simpleName) {
      consume(it.mapTo(MatchStarted::class.java))
    }

    eventBus.on(CardDrew::class.java.simpleName) {
      consume(it.mapTo(CardDrew::class.java))
    }

    eventBus.on(MonsterNormalSummoned::class.java.simpleName) {
      consume(it.mapTo(MonsterNormalSummoned::class.java))
    }

    eventBus.on(MonsterDestroyed::class.java.simpleName) {
      consume(it.mapTo(MonsterDestroyed::class.java))
    }

    eventBus.on(MatchEnded::class.java.simpleName) {
      consume(it.mapTo(MatchEnded::class.java))
    }
  }
  
  private fun consume(event: MatchEnded) {
    println("Player ${event.winner} won")
  }
  
  private fun consume(event: MonsterDestroyed) {
    println("Monster Destroyed")
  }
  
  private fun consume(event: MatchStarted) {
    decksCount[event.player.username] = event.player.deckSize
    decksCount[event.opponent.username] = event.opponent.deckSize
    
    println("Player ${event.player.username} start the match")
    println("Player ${event.opponent.username} start the match")
  }
  
  private fun consume(event: CardDrew) {
    decksCount[event.by] = decksCount[event.by]!! - 1
    println("player ${event.by} has drawn a card. Cards remaining: ${decksCount[event.by]}")
  }
  
  private fun consume(event: MonsterNormalSummoned) {
    println("Player: ${event.by} summoned monster: ${event.monster.name}")
  }
  
}