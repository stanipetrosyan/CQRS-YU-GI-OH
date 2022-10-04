import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import port.EventBus

class DefaultEventBus(private val eventBus: io.vertx.core.eventbus.EventBus) : EventBus {
  override fun emit(address: String, message: JsonObject): Future<Unit> {
    eventBus.publish(address, message)
    return Future.succeededFuture()
  }
  
  override fun on(address: String, consume: Handler<JsonObject>){
    eventBus.localConsumer<JsonObject>(address) {
      consume.handle(it.body())
    }
  }
}
