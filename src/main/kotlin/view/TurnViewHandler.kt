package view

import domain.*
import port.EventBus

class TurnViewHandler(eventBus: EventBus) {
  
  private var turn: Turn = Turn(1, Turn.State.DrawPhase)
  
  init {
    eventBus.on(DrawPhaseSet::class.java.simpleName) {
      consume(it.mapTo(DrawPhaseSet::class.java))
    }
    eventBus.on(StandbyPhaseSet::class.java.simpleName) {
      consume(it.mapTo(StandbyPhaseSet::class.java))
    }
    eventBus.on(MainPhaseOneSet::class.java.simpleName) {
      consume(it.mapTo(MainPhaseOneSet::class.java))
    }
    eventBus.on(BattlePhaseSet::class.java.simpleName) {
      consume(it.mapTo(BattlePhaseSet::class.java))
    }
    eventBus.on(MainPhaseTwoSet::class.java.simpleName) {
      consume(it.mapTo(MainPhaseTwoSet::class.java))
    }
    eventBus.on(EndPhaseSet::class.java.simpleName) {
      consume(it.mapTo(EndPhaseSet::class.java))
    }
  }
  
  private fun consume(event: DrawPhaseSet) {
    turn = turn.copy(state = Turn.State.DrawPhase)
    
    println("player ${event.by} set ${turn.state}")
  }
  private fun consume(event: StandbyPhaseSet) {
    turn = turn.copy(state = Turn.State.StandByPhase)
  
    println("player ${event.by} set ${turn.state}")
  }
  private fun consume(event: MainPhaseOneSet) {
    turn = turn.copy(state = Turn.State.MainPhase1)
    
    println("player ${event.by} set ${turn.state}")
  }
  private fun consume(event: BattlePhaseSet) {
    turn = turn.copy(state = Turn.State.BattlePhase)
    
    println("player ${event.by} set ${turn.state}")
  }
  private fun consume(event: MainPhaseTwoSet) {
    turn = turn.copy(state = Turn.State.MainPhase2)
    
    println("player ${event.by} set ${turn.state}")
  }
  private fun consume(event: EndPhaseSet) {
    turn = turn.copy(state = Turn.State.EndPhase)
    
    println("player ${event.by} set ${turn.state}")
  }
}