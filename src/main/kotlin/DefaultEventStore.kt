import domain.MatchEvent
import port.EventStore
import java.util.*

class ListEventStore: EventStore {
  private val list = mutableListOf<MatchEvent>()
  
  override fun add(event: MatchEvent) {
    list.add(event)
  }
  
  override fun findBy(id: UUID): List<MatchEvent> {
    return list.filter { it.matchId == id }
  }
  
}
