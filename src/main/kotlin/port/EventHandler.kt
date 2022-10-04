package port

import domain.Event

interface EventHandler<E: Event> {
  fun consume(event: E)
}