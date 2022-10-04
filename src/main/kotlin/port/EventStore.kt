package port

import domain.MatchEvent
import java.util.*

interface EventStore {
  fun add(event: MatchEvent)
  fun findBy(id: UUID): List<MatchEvent>
}