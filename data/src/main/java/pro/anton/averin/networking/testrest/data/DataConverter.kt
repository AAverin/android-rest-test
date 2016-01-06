package pro.anton.averin.networking.testrest.data

import pro.anton.averin.networking.testrest.data.models.Request
import pro.anton.averin.networking.testrest.data.models.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class DataConverter @Inject constructor() {

    public fun toOkhttp(requestModel: Request): okhttp3.Request {
        val builder = okhttp3.Request.Builder()

        builder.url(requestModel.asURI())
        builder.method(requestModel.method, null)
        requestModel.headers.forEach { header ->
            builder.addHeader(header.name, header.value)
        }

        return builder.build()
    }

    public fun fromOkHttp(okhttpResponse: okhttp3.Response): Response {
        val response = Response()

        response.status = okhttpResponse.code()
        response.method = okhttpResponse.protocol().name
        response.url = okhttpResponse.request().url().toString()
        response.body = okhttpResponse.body().string()
        response.headers = okhttpResponse.headers().toMultimap()

        return response
    }

}


