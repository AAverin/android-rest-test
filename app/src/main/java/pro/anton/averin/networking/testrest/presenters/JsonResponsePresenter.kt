package pro.anton.averin.networking.testrest.presenters

import android.os.Bundle
import pro.anton.averin.networking.testrest.data.Storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonResponsePresenter
@Inject
constructor() : BasePresenter<JsonResponseView>() {

    @Inject
    lateinit var storage: Storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view.update(storage.currentResponse)
    }
}
