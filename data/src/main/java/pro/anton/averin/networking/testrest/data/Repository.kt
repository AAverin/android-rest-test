package pro.anton.averin.networking.testrest.data

import pro.anton.averin.networking.testrest.data.models.Headers
import pro.anton.averin.networking.testrest.data.models.Request
import rx.Observable

interface Repository {
    fun getSupportedHeaders(): List<Headers.Header>
    fun updateHeader(header: Headers.Header): Boolean

    fun sendRequest(request: Request): Observable<String>
}