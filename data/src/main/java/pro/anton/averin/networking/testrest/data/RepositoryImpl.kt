package pro.anton.averin.networking.testrest.data

import pro.anton.averin.networking.testrest.data.models.Headers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor() : Repository {
    override fun getSupportedHeaders(): List<Headers.Header> {
        throw UnsupportedOperationException()
    }

    override fun updateHeader(header: Headers.Header): Boolean {
        throw UnsupportedOperationException()
    }

}

