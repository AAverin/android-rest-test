package pro.anton.averin.networking.testrest.data

import pro.anton.averin.networking.testrest.data.models.Request
import pro.anton.averin.networking.testrest.data.models.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class Storage @Inject constructor() {

    public var currentRequest: Request? = null
    public var currentResponse: Response? = null

}


