package pro.anton.averin.networking.testrest.navigation

import pro.anton.averin.networking.testrest.di.ActivityScope
import pro.anton.averin.networking.testrest.rx.UiBus
import pro.anton.averin.networking.testrest.rx.events.NaviResponseScreenEvent
import pro.anton.averin.networking.testrest.utils.IntentBuilder
import pro.anton.averin.networking.testrest.views.base.BaseActivity
import javax.inject.Inject

@ActivityScope
class UINavigator
@Inject
constructor() {

    @Inject
    lateinit var baseActivity: BaseActivity
    @Inject
    lateinit var uiBus: UiBus
    @Inject
    lateinit var intentBuilder: IntentBuilder

    fun navigateToResponseScreen() {
        uiBus.post(NaviResponseScreenEvent())
    }

    fun navigateToManagerScreenForSave() {
        baseActivity.startActivity(intentBuilder.intentToManager_withSave())
    }

    fun navigateToManagerScreen() {
        baseActivity.startActivity(intentBuilder.intentToManager_withoutSave())
    }
}
