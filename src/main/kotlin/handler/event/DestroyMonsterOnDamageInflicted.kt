package handler.event

import Matches
import domain.BattleDamageInflicted
import domain.DestroyMonster
import port.CommandBus
import port.EventBus
import port.EventHandler

class DestroyMonsterOnDamageInflicted(eventBus: EventBus, private val commandBus: CommandBus, private val matches: Matches): EventHandler<BattleDamageInflicted> {
  
  init {
    eventBus.on(BattleDamageInflicted::class.java.simpleName) {
      consume(it.mapTo(BattleDamageInflicted::class.java))
    }
  }
  
  override fun consume(event: BattleDamageInflicted) {
    val match = matches.load(event.matchId)
    
    val monsterToDestroy = match.monsterToDestroy()
    if (monsterToDestroy != null)
      commandBus.send(DestroyMonster(event.matchId, event.by, monsterToDestroy))
  }
  
}