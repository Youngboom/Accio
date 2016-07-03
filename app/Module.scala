import com.google.inject.AbstractModule

import services._

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[HttpClientService]).to(classOf[HttpClientLiveService])
    bind(classOf[CipherService]).to(classOf[CipherLiveService])
  }
}
