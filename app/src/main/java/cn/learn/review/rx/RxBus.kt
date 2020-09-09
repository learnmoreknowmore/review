package cn.learn.review.rx

import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * RxBus代替EventBus
 */
class RxBus private constructor() {
    private val rxBus = PublishSubject.create<Any>().toSerialized()

    object RxBusHolder {
        val instance = RxBus()
    }

    fun hasObservable(): Boolean {
        return rxBus.hasObservers()
    }

    fun send(o: Bundle) {
        rxBus.onNext(o)
    }

    fun toObservable(): Observable<Any> {
        return rxBus
    }

    companion object {
        val instance: RxBus
            get() = RxBusHolder.instance
    }
}