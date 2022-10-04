package view

import domain.*
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject

class TurnViewHandler(eventBus: EventBus) {
  
  private var turn: Turn = Turn(1, Turn.State.DrawPhase)
  
  init {
    eventBus.consumer<JsonObject>(DrawPhaseSet::class.simpleName) {
      consume(it.body().mapTo(DrawPhaseSet::class.java))
    }
    eventBus.consumer<JsonObject>(StandbyPhaseSet::class.simpleName) {
      consume(it.body().mapTo(StandbyPhaseSet::class.java))
    }
    eventBus.consumer<JsonObject>(MainPhaseOneSet::class.simpleName) {
      consume(it.body().mapTo(MainPhaseOneSet::class.java))
    }
    eventBus.consumer<JsonObject>(BattlePhaseSet::class.simpleName) {
      consume(it.body().mapTo(BattlePhaseSet::class.java))
    }
    eventBus.consumer<JsonObject>(MainPhaseTwoSet::class.simpleName) {
      consume(it.body().mapTo(MainPhaseTwoSet::class.java))
    }
    eventBus.consumer<JsonObject>(EndPhaseSet::class.simpleName) {
      consume(it.body().mapTo(EndPhaseSet::class.java))
    }
  }
  
  private fun consume(event: DrawPhaseSet) {
    turn = turn.copy(state = Turn.State.DrawPhase)
    
    println("player ${event.by} è entrato in ${turn.state}")
  }
  private fun consume(event: StandbyPhaseSet) {
    turn = turn.copy(state = Turn.State.StandByPhase)
  
    println("player ${event.by} è entrato in ${turn.state}")
  }
  private fun consume(event: MainPhaseOneSet) {
    turn = turn.copy(state = Turn.State.MainPhase1)
    
    println("player ${event.by} è entrato in ${turn.state}")
  }
  private fun consume(event: BattlePhaseSet) {
    turn = turn.copy(state = Turn.State.BattlePhase)
    
    println("player ${event.by} è entrato in ${turn.state}")
  }
  private fun consume(event: MainPhaseTwoSet) {
    turn = turn.copy(state = Turn.State.MainPhase2)
    
    println("player ${event.by} è entrato in ${turn.state}")
  }
  private fun consume(event: EndPhaseSet) {
    turn = turn.copy(state = Turn.State.EndPhase)
    
    
    println("player ${event.by} è entrato in ${turn.state}")
  }
}