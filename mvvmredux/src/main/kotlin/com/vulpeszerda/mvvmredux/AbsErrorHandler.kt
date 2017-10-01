package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.addon.bufferUntilOnResumed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsErrorHandler<E : ReduxEvent>(private val owner: LifecycleOwner) : ErrorHandler {

    private val eventSubject = PublishSubject.create<E>()

    val events = eventSubject.hide()!!

    protected fun emitAction(action: E) {
        eventSubject.onNext(action)
    }

    override fun subscribe(source: Observable<ReduxEvent.Error>) {
        source.bufferUntilOnResumed(owner).subscribe(this::onError) { throwable ->
            onError(ReduxEvent.Error(throwable))
        }
    }
}