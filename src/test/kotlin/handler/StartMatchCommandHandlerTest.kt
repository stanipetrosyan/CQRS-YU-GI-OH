package handler

import Matches
import domain.MatchStarted
import domain.Player
import domain.StartMatch
import handler.command.StartMatchCommandHandler
import io.mockk.*
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import port.CommandResult
import java.time.LocalDateTime
import java.util.*

internal class StartMatchCommandHandlerTest {
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
  internal fun `should handle start match command`() {
    val matchId = UUID.randomUUID()
    val player = Player("aPlayer", 40, 8000)
    every { LocalDateTime.now() } returns now
    every { matches.save(any())} returns Unit
  
    val result = StartMatchCommandHandler(matches).handle(StartMatch(matchId, player.username, player, player))
    
    assertThat(result, CoreMatchers.`is`(CommandResult.OK))
    verify { matches.save(match {
      it.changes.contains(MatchStarted(matchId, player, player, now))
    }) }
  }
}