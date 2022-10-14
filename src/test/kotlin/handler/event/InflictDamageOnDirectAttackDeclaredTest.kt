package handler.event

import DefaultEventBus
import JsonExtension
import Matches
import domain.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import port.CommandBus
import port.CommandResult
import port.EventBus
import java.time.LocalDateTime
import java.util.*

@ExtendWith(VertxExtension::class, JsonExtension::class)
internal class InflictDamageOnDirectAttackDeclaredTest {
  private lateinit var eventBus: EventBus
  private val commandBus: CommandBus = mockk()
  private val matches: Matches = mockk()
  
  @BeforeEach
  internal fun setUp(vertx: Vertx) {
    eventBus = DefaultEventBus(vertx.eventBus())
  }
  
  @Test
  internal fun `should emit inflict damage command`(test: VertxTestContext) {
    val matchId = UUID.randomUUID()
    val monster = Monster(UUID.randomUUID(), "nName", 7, 2000, 2000, MonsterType.Normal, "aDescription")
    val now = LocalDateTime.now()
    val by = "username"
    
    every { commandBus.send(any()) } returns CommandResult.OK
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(by, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      MainPhaseOneSet(matchId, by, now),
      MonsterNormalSummoned(matchId, monster, now),
      BattlePhaseSet(matchId, by, now),
      DirectAttackDeclared(matchId, monster.id, by, now)
    ))
    
    InflictDamageOnDirectAttackDeclared(eventBus, commandBus, matches)
  
    val event = DirectAttackDeclared(matchId, monster.id, by, now)
    eventBus.emit(DirectAttackDeclared::class.java.simpleName, JsonObject.mapFrom(event))
    
    await().untilAsserted {
      verify { commandBus.send(InflictDamage(matchId, "username", 2000, DamageType.Direct)) }
      test.completeNow()
    }
  }
}