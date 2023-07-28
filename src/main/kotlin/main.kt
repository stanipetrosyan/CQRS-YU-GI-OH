import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import domain.*
import handler.event.*
import io.vertx.core.Vertx
import io.vertx.core.json.jackson.DatabindCodec
import view.FieldViewHandler
import view.LifePointsViewHandler
import view.TurnViewHandler
import java.util.*


fun main() {
  DatabindCodec.mapper()
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .registerModule(JavaTimeModule())
    .registerModule(KotlinModule())
  
  // setting command handler, event handler and aggregations
  val vertx = Vertx.vertx()
  val eventBus = vertx.eventBus()
  val myEventBus = DefaultEventBus(eventBus)
  val matches = EventSourcedMatches(ListEventStore(), eventBus)
  val commandBus = DefaultCommandBus(matches)
  
  // events Handler
  FieldViewHandler(eventBus)
  LifePointsViewHandler(eventBus)
  TurnViewHandler(eventBus)
  ApplyEffectOnSpellActivated(myEventBus, commandBus)
  InflictDamageOnDirectAttackDeclared(myEventBus, commandBus, matches)
  InflictDamageOnAttackDeclared(myEventBus, commandBus, matches)
  DestroyMonsterOnDamageInflicted(myEventBus, commandBus, matches)
  EndMatchOnDamageInflicted(myEventBus, commandBus, matches)
  
  // set up
  val player = Player("home", 40, 8000)
  val opponent = Player("opponent", 40, 8000)
  val skullId = UUID.randomUUID()
  val monster = Monster(skullId, "Summoned Skull", 7, 2500, 1000, MonsterType.Normal, "A fiend with dara powers...")
  val dragonId = UUID.randomUUID()
  val anotherMonster = Monster(dragonId, "Blue-Eyes White Dragon", 8, 3000, 2500, MonsterType.Normal, "This legendary dragon is a powerful engine of destruction.")
  val finalMonsterId = UUID.randomUUID()
  val finalMonster = Monster(finalMonsterId, "Monster", 8, 8500, 2500, MonsterType.Normal, "Monster for finish game")
  val potSpell = Spell("Pot Of Greed", Spell.Type.Normal, "Draw 2 cards from your deck", listOf(DrawCards(2)))
  val tributeSpell = Spell("Tribute To The Doomed", Spell.Type.Normal, "Discard 1 card. Destroy 1 monster on the field", listOf(DiscardCards(1), DestroyCard(skullId)))
  
  // start the match
  val matchId = UUID.randomUUID()
  commandBus.send(StartMatch(matchId, player.username, player, opponent))
  
  // turn 1
  commandBus.send(SetDrawPhase(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(DrawCard(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(SetStandByPhase(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(SetMainPhaseOne(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(ActiveSpell(matchId, player.username, potSpell))
  Thread.sleep(1000)
  commandBus.send(NormalSummonMonster(matchId, player.username, monster))
  Thread.sleep(1000)
  commandBus.send(SetBattlePhase(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(DeclareDirectAttack(matchId, player.username, opponent.username, skullId))
  Thread.sleep(1000)
  
  commandBus.send(SetEndPhase(matchId, player.username))
  
  // turn 2
  commandBus.send(SetDrawPhase(matchId, opponent.username))
  Thread.sleep(1000)
  commandBus.send(DrawCard(matchId, opponent.username))
  Thread.sleep(1000)
  commandBus.send(SetStandByPhase(matchId, opponent.username))
  Thread.sleep(1000)
  commandBus.send(SetMainPhaseOne(matchId, opponent.username))
  Thread.sleep(1000)
  commandBus.send(NormalSummonMonster(matchId, opponent.username, anotherMonster))
  Thread.sleep(1000)
  commandBus.send(SetBattlePhase(matchId, opponent.username))
  Thread.sleep(1000)
  commandBus.send(DeclareAttack(matchId, opponent.username, player.username, anotherMonster.id, monster.id))
  Thread.sleep(1000)
  
  commandBus.send(SetEndPhase(matchId, player.username))
  
  // turn 3
  commandBus.send(SetDrawPhase(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(DrawCard(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(SetStandByPhase(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(SetMainPhaseOne(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(ActiveSpell(matchId, player.username, tributeSpell))
  Thread.sleep(1000)
  commandBus.send(NormalSummonMonster(matchId, player.username, finalMonster))
  Thread.sleep(1000)
  commandBus.send(SetBattlePhase(matchId, player.username))
  Thread.sleep(1000)
  commandBus.send(DeclareDirectAttack(matchId, player.username, opponent.username, finalMonsterId))
  Thread.sleep(1000)
}