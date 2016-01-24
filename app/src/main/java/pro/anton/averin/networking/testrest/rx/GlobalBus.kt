package pro.anton.averin.networking.testrest.rx

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalBus @Inject constructor(): RxBus() {
}