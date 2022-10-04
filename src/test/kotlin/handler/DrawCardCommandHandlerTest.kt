package handler

import Matches
import domain.*
import handler.command.DrawCardCommandHandler
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class DrawCardCommandHandlerTest {
  
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
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player(username, 40, 8000), now)
    ))
    every { matches.save(any())} returns Unit
  
    DrawCardCommandHandler(matches).handle(DrawCard(matchId, username))
    
    verify { matches.save(match {
      it.changes.contains(CardDrew(matchId, username, now))
    }) }
  }
}