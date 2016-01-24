package pro.anton.averin.networking.testrest.navigation

import javax.inject.Inject

import pro.anton.averin.networking.testrest.di.ActivityScope
import pro.anton.averin.networking.testrest.rx.RxBus
import pro.anton.averin.networking.testrest.rx.events.NaviResponseScreenEvent
import pro.anton.averin.networking.testrest.utils.IntentBuilder
import pro.anton.averin.networking.testrest.views.base.BaseActivity
import javax.inject.Named

@ActivityScope
class Navigator
@Inject
constructor() {

    @Inject
    lateinit var baseActivity: BaseActivity
    @Inject
    lateinit var rxBus: RxBus
    @Inject
    lateinit var intentBuilder: IntentBuilder

    fun navigateToResponseScreen() {
        rxBus.post(NaviResponseScreenEvent())
    }

    fun navigateToManagerScreenForSave() {
        baseActivity.startActivity(intentBuilder.intentToManager_withSave())
    }

    fun navigateToManagerScreen() {
        baseActivity.startActivity(intentBuilder.intentToManager_withoutSave())
    }
}
