package handler.command

import Matches
import domain.*
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class SetMonsterCommandHandlerTest {
  private val matches: Matches = mockk()
  private val now = LocalDateTime.now()
  
  @BeforeEach
  internal fun setUp() {
    mockkStatic(LocalDateTime::class)
  }
  
  @AfterEach
  internal fun tearDown() {
    unmockkStatic(LocalDateTime::class)
  }
  
  @Test
  internal fun `should handle set monster command`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player(username, 40, 8000), now),
      MainPhaseOneSet(matchId, username, now)
    ))
    every { matches.save(any())} returns Unit
    
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
    SetMonsterCommandHandler(matches).handle(SetMonster(matchId, username, monster))
    
    verify { matches.save(match {
      it.changes.contains(MonsterSet(matchId, username, monster, now))
    }) }
  }
  
  @Test
  internal fun `should not permit handle summon monster twice`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player(username, 40, 8000), now),
      MonsterNormalSummoned(matchId, username, monster, now)
    ))
    every { matches.save(any())} returns Unit
    
    SetMonsterCommandHandler(matches).handle(SetMonster(matchId, username, monster))
    
    verify(exactly = 0) { matches.save(any())}
  }
  
  @Test
  internal fun `should not permit handle summon monster normal out of main phase`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player(username, 40, 8000), now),
    ))
    every { matches.save(any())} returns Unit
    
    SetMonsterCommandHandler(matches).handle(SetMonster(matchId, username, monster))
    
    verify(exactly = 0) { matches.save(any())}
  }
}