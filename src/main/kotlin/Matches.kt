import domain.Match
import io.vertx.core.json.JsonObject
import port.EventBus
import port.EventStore
import java.util.*

interface Matches {
  fun load(id: UUID): Match
  fun save(match: Match)
}

class EventSourcedMatches(private val eventStore: EventStore, private val eventBus: EventBus): Matches {
  override fun load(id: UUID): Match {
    val events = eventStore.findBy(id)
    return Match(id).hydrate(events)
  }
  
  override fun save(match: Match) {
    match.changes.forEach { event ->
      eventStore.add(event)
      println("[${event.javaClass.simpleName}] Event emitted")
      eventBus.emit(event.javaClass.simpleName, JsonObject.mapFrom(event))
    }
  }
  
}