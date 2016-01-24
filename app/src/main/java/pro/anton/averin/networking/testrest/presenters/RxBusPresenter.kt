package pro.anton.averin.networking.testrest.presenters

import javax.inject.Inject

import pro.anton.averin.networking.testrest.BaseContext
import pro.anton.averin.networking.testrest.rx.GlobalBus
import pro.anton.averin.networking.testrest.utils.LLogger
import rx.Subscription
import rx.functions.Action1

abstract class RxBusPresenter<B : BaseView>(baseContext: BaseContext) : BasePresenterImpl<B>(baseContext) {

    @Inject
    lateinit var globalBus: GlobalBus
    @Inject
    lateinit var llogger: LLogger

    private var subscription: Subscription? = null

    override fun onResume() {
        super.onResume()
        if (isVisible) {
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
