package pro.anton.averin.networking.testrest.presenters

import pro.anton.averin.networking.testrest.data.Repository
import pro.anton.averin.networking.testrest.data.models.Headers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddHeaderPopupPresenter
@Inject
constructor() : BasePresenter<AddHeaderPopupView>() {

    @Inject
    lateinit var repository: Repository

    val supportedHeaders: List<Headers.Header>
        get() = repository.getSupportedHeaders()

    fun updateHeader(selectedHeader: Headers.Header): Boolean {
        return repository.updateHeader(selectedHeader)
    }
}
