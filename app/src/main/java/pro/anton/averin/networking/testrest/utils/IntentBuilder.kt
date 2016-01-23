package pro.anton.averin.networking.testrest.utils

import android.content.Intent
import pro.anton.averin.networking.testrest.BaseContext
import pro.anton.averin.networking.testrest.di.ActivityScope
import pro.anton.averin.networking.testrest.views.activities.TestRestActivity
import javax.inject.Inject

@ActivityScope
class IntentBuilder @Inject constructor() {

    @Inject lateinit var baseContext: BaseContext

    fun intentToManager_withSave(): Intent {
        val intent = Intent(baseContext, TestRestActivity::class.java)

        return intent
    }

    fun intentToManager_withoutSave(): Intent {
        val intent = Intent(baseContext, TestRestActivity::class.java)

        return intent
    }
}


