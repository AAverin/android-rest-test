package pro.anton.averin.networking.testrest.data.network

import okhttp3.OkHttpClient
import pro.anton.averin.networking.testrest.data.models.Request
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Network @Inject constructor() {

    @Inject
    lateinit var okhttp: OkHttpClient

    fun sendRequest(request: Request): Observable<String> {
        request.baseUrl = ""
        return Observable.empty()
    }


}


