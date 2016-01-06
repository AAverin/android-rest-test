package pro.anton.averin.networking.testrest.data

import okhttp3.Response
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
    @Inject
    lateinit var storage: Storage
    @Inject
    lateinit var dataConverter: DataConverter

    override fun sendRequest(request: Request): Observable<Response> {
        return network.sendRequest(dataConverter.toOkhttp(request)).map { okHttpResponse ->
            storage.currentResponse = dataConverter.fromOkHttp(okHttpResponse)
            okHttpResponse
        }
    }

    override fun getSupportedHeaders(): List<Headers.Header> {
        throw UnsupportedOperationException()
    }

    override fun updateHeader(header: Headers.Header): Boolean {
        throw UnsupportedOperationException()
    }

}

