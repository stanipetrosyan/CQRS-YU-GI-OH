package handler.command

import Matches
import domain.*
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ActiveSpellCommandHandlerTest {
  
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
  fun `should active spell effect`() {
    val matchId = UUID.randomUUID()
    val username = "aPlayer"
    every { LocalDateTime.now() } returns now
    every { matches.load(matchId) } returns Match(matchId).hydrate(listOf(
      MatchStarted(matchId, Player(username, 40, 8000), Player(username, 40, 8000), now),
      MainPhaseOneSet(matchId, username, now)
    ))
    every { matches.save(any())} returns Unit
  
    val spell = Spell("aSpell", Spell.Type.Normal, "aText", Spell.Effect(DrawCards(2)))
    ActiveSpellCommandHandler(matches).handle(ActiveSpell(matchId, username, spell))
  
    verify { matches.save(match {
      it.changes.contains(SpellActivated(matchId, username, spell, now))
    }) }
  }
}