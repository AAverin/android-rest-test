package pro.anton.averin.networking.testrest.data

import pro.anton.averin.networking.testrest.data.models.Headers

interface Repository {
    fun getSupportedHeaders(): List<Headers.Header>
    fun updateHeader(header: Headers.Header): Boolean
}