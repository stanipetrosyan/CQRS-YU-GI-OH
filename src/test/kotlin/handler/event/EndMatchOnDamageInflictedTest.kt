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
import org.awaitility.Awaitility
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import port.CommandBus
import port.CommandResult
import port.EventBus
import java.time.LocalDateTime
import java.util.*

@ExtendWith(VertxExtension::class, JsonExtension::class)
internal class EndMatchOnDamageInflictedTest {
  private lateinit var eventBus: EventBus
  private val commandBus: CommandBus = mockk()
  private val matches: Matches = mockk()
  
  @BeforeEach
  internal fun setUp(vertx: Vertx) {
    eventBus = DefaultEventBus(vertx.eventBus())
  }
  
  @Test
  internal fun `should send end match command if life points of player are equal or less 0 with battle damage`(test: VertxTestContext) {
    val matchId = UUID.randomUUID()
    val now = LocalDateTime.now()
    val by = "username"
    
    every { commandBus.send(any()) } returns CommandResult.OK
    every { matches.load(matchId) } returns Match(matchId).hydrate(
      listOf(
        MatchStarted(matchId, Player(by, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      )
    )
    
    EndMatchOnDamageInflicted(eventBus, commandBus, matches)
    
    val event = BattleDamageInflicted(matchId, by, 8000, now)
    eventBus.emit(BattleDamageInflicted::class.java.simpleName, JsonObject.mapFrom(event))
    
    Awaitility.await().untilAsserted {
      verify { commandBus.send(EndMatch(matchId, "anotherPlayer")) }
      test.completeNow()
    }
  }
  
  @Test
  internal fun `should send end match command if life points of player are equal or less 0 with direct damage`(test: VertxTestContext) {
    val matchId = UUID.randomUUID()
    val now = LocalDateTime.now()
    val by = "username"
    
    every { commandBus.send(any()) } returns CommandResult.OK
    every { matches.load(matchId) } returns Match(matchId).hydrate(
      listOf(
        MatchStarted(matchId, Player(by, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      )
    )
    
    EndMatchOnDamageInflicted(eventBus, commandBus, matches)
    
    val event = DirectDamageInflicted(matchId, by, 8000, now)
    eventBus.emit(DirectDamageInflicted::class.java.simpleName, JsonObject.mapFrom(event))
    
    Awaitility.await().untilAsserted {
      verify { commandBus.send(EndMatch(matchId, "anotherPlayer")) }
      test.completeNow()
    }
  }
}