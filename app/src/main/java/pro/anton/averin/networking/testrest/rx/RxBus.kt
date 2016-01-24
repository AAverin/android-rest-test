package pro.anton.averin.networking.testrest.rx

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import javax.inject.Singleton

@Singleton
public open class RxBus {
    private val bus = SerializedSubject<Any, Any>(PublishSubject.create());

    public fun post(event: Any) {
        bus.onNext(event)
    }

    public fun <T> events(type: Class<T>): Observable<T> {
        return events().ofType(type)
    }

    public fun events(): Observable<Any> {
        return bus.asObservable()
    }
}

