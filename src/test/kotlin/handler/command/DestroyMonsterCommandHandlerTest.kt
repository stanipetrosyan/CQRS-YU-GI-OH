package handler.command

import Matches
import domain.*
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class DestroyMonsterCommandHandlerTest {
  
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
  internal fun `should handle draw card command`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    val monsterId = UUID.randomUUID()
    val monster = Monster(monsterId, "aMonster", 8, 2000, 1000, MonsterType.Normal, "aDescription")
    
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(
      listOf(
        MatchStarted(matchId, Player(username, 40, 8000), Player(username, 40, 8000), now),
        MainPhaseOneSet(matchId, username, now),
        MonsterNormalSummoned(matchId, username, monster, now)
      )
    )
    every { matches.save(any()) } returns Unit
  
    DestroyMonsterCommandHandler(matches).handle(DestroyMonster(matchId, username, monsterId))
    
    verify {
      matches.save(match {
        it.changes.contains(MonsterDestroyed(matchId, username, monsterId, now))
      })
    }
  }
}