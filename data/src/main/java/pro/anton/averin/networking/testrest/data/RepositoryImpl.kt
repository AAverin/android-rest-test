package pro.anton.averin.networking.testrest.data

import pro.anton.averin.networking.testrest.data.models.Headers
import pro.anton.averin.networking.testrest.data.models.Request
import pro.anton.averin.networking.testrest.data.network.Network
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor() : Repository {

    @Inject
    lateinit var network: Network

    override fun sendRequest(request: Request): Observable<String> {
        return network.sendRequest(request)
    }

    override fun getSupportedHeaders(): List<Headers.Header> {
        throw UnsupportedOperationException()
    }

    override fun updateHeader(header: Headers.Header): Boolean {
        throw UnsupportedOperationException()
    }

}

