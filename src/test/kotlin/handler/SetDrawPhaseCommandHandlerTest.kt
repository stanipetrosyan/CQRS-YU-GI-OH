package handler

import Matches
import domain.*
import handler.command.SetDrawPhaseCommandHandler
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class SetDrawPhaseCommandHandlerTest {
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
  internal fun `should handle set draw phase command`() {
    val matchId = UUID.randomUUID()
    val player = Player("aPlayer", 40, 8000)
    every { LocalDateTime.now() } returns now
    every { matches.save(any()) } returns Unit
    every { matches.load(matchId) } returns Match(matchId).hydrate(
      listOf(
        MatchStarted(matchId, player, player, now),
        EndPhaseSet(matchId, player.username, now)
      )
    )
    
    SetDrawPhaseCommandHandler(matches).handle(
      SetDrawPhase(matchId, player.username)
    )
    
    verify {
      matches.save(match {
        it.changes.contains(DrawPhaseSet(matchId, player.username, now))
      })
    }
  }
  
  @Test
  internal fun `should not handle set main phase command if state is prec than actual`() {
    val matchId = UUID.randomUUID()
    val player = Player("aPlayer", 40, 8000)
    every { LocalDateTime.now() } returns now
    every { matches.save(any()) } returns Unit
    every { matches.load(matchId) } returns Match(matchId).hydrate(
      listOf(
        MatchStarted(matchId, player, player, now),
        MainPhaseOneSet(matchId, player.username, now)
      )
    )
  
    SetDrawPhaseCommandHandler(matches).handle(SetDrawPhase(matchId, player.username))
    
    verify(exactly = 0) { matches.save(any()) }
  }
}