package port

import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject

interface EventBus {
  fun emit(address: String, message: JsonObject): Future<Unit>
  fun on(address: String, consume: Handler<JsonObject>)
}
