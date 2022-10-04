package handler

import Matches
import domain.*
import handler.command.DeclareDirectAttackCommandHandler
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class DeclareDirectAttackCommandHandlerTest {
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
  internal fun `should handle direct attack`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
  
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      MainPhaseOneSet(matchId, username, now),
      MonsterNormalSummoned(matchId, monster, now),
      BattlePhaseSet(matchId, username, now)
    ))
    every { matches.save(any())} returns Unit
    
    DeclareDirectAttackCommandHandler(matches).handle(DeclareDirectAttack(matchId, username, "anotherPlayer", monster.id))
    
    verify { matches.save(match {
      it.changes.contains(DirectAttackDeclared(matchId, monster.id, "anotherPlayer", now))
    }) }
  }
  
  @Test
  internal fun `should do nothing if state of turn is not battle phase`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
  
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      MainPhaseOneSet(matchId, username, now),
      MonsterNormalSummoned(matchId, monster, now),
    ))
    
    DeclareDirectAttackCommandHandler(matches).handle(DeclareDirectAttack(matchId, username, "anotherPlayer", monster.id))
    
    verify(exactly = 0) { matches.save(any()) }
  }
  
  @Test
  internal fun `should do nothing if monster already attacked`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
  
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      MainPhaseOneSet(matchId, username, now),
      MonsterNormalSummoned(matchId, monster, now),
      BattlePhaseSet(matchId, username, now),
      DirectAttackDeclared(matchId, monster.id, username, now)
    ))
    
    DeclareDirectAttackCommandHandler(matches).handle(DeclareDirectAttack(matchId, username, "anotherPlayer", monster.id))
    
    verify(exactly = 0) { matches.save(any()) }
  }
  
}