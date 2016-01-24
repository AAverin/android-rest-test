package pro.anton.averin.networking.testrest.presenters

import pro.anton.averin.networking.testrest.rx.GlobalBus
import pro.anton.averin.networking.testrest.utils.LLogger
import rx.Subscription
import javax.inject.Inject

abstract class RxBusPresenter<B : BaseView>() : BasePresenter<B>() {

    @Inject
    lateinit var globalBus: GlobalBus
    @Inject
    lateinit var llogger: LLogger

    private var subscription: Subscription? = null

    override fun onResume() {
        super.onResume()
        if (visible) {
            subscribe()
        }
    }

    override fun onVisible() {
        super.onVisible()
        subscribe()
    }

    override fun onHidden() {
        super.onHidden()
        unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }

    private fun subscribe() {
        subscription = globalBus.events().subscribe {
            onEvent(it)
        }

    }

    protected abstract fun onEvent(event: Any)

    private fun unsubscribe() {
        subscription?.unsubscribe()
    }
}
