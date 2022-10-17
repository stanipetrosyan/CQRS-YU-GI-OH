package handler

import Matches
import domain.*
import handler.command.DeclareAttackCommandHandler
import io.mockk.*
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import port.CommandResult
import java.time.LocalDateTime
import java.util.*

internal class DeclareAttackCommandHandlerTest {
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
  internal fun `should handle attack`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      BattlePhaseSet(matchId, username, now)
    ))
    every { matches.save(any())} returns Unit
    
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 2500, 1000, MonsterType.Normal, "aDescription")
    val anotherMonster = Monster(UUID.randomUUID(), "anotherMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
    DeclareAttackCommandHandler(matches).handle(DeclareAttack(matchId, username, "anotherPlayer", monster.id, anotherMonster.id))
    
    verify { matches.save(match {
      it.changes.contains(AttackDeclared(matchId, monster.id, anotherMonster.id, username, now))
    }) }
  }
  
  @Test
  internal fun `should do nothing if state of turn is not battle phase`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      MainPhaseOneSet(matchId, username, now)
    ))
    every { matches.save(any())} returns Unit
  
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 2500, 1000, MonsterType.Normal, "aDescription")
    val anotherMonster = Monster(UUID.randomUUID(), "anotherMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
    val result = DeclareAttackCommandHandler(matches).handle(DeclareAttack(matchId, username, "anotherPlayer", monster.id, anotherMonster.id))
  
    assertThat(result, CoreMatchers.`is`(CommandResult.MonsterCannotAttack))
    verify(exactly = 0) { matches.save(any()) }
  }
  
  @Test
  internal fun `should do nothing if monster already attacked`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    val monster = Monster(UUID.randomUUID(), "aMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
    val anotherMonster = Monster(UUID.randomUUID(), "anotherMonster", 4, 1000, 1000, MonsterType.Normal, "aDescription")
  
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player("anotherPlayer", 40, 8000), now),
      MainPhaseOneSet(matchId, username, now),
      MonsterNormalSummoned(matchId, username, monster, now),
      BattlePhaseSet(matchId, username, now),
      AttackDeclared(matchId, monster.id, anotherMonster.id, username, now)
    ))
  
    val result = DeclareAttackCommandHandler(matches).handle(DeclareAttack(matchId, username, "anotherPlayer", monster.id, anotherMonster.id))
    
    assertThat(result, CoreMatchers.`is`(CommandResult.MonsterCannotAttack))
    verify(exactly = 0) { matches.save(any()) }
  }
  
}