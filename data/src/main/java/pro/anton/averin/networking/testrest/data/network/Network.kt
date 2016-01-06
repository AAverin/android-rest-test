package pro.anton.averin.networking.testrest.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Network @Inject constructor() {

    @Inject
    lateinit var okhttp: OkHttpClient

    fun sendRequest(request: Request): Observable<Response> {

        return Observable.create<Response> { subscriber ->
            val networkResponse = okhttp.newCall(request).execute();

            subscriber.onNext(networkResponse)
            subscriber.onCompleted()
        }



    }


}


