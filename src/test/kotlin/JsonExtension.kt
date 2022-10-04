import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.vertx.core.json.jackson.DatabindCodec
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class JsonExtension: BeforeAllCallback, ExtensionContext.Store.CloseableResource {

  override fun close() {
  }

  override fun beforeAll(context: ExtensionContext) {
    DatabindCodec.mapper()
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
      .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
      .registerModule(JavaTimeModule())
      .registerKotlinModule()
  }

}
