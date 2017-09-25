package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.library.addon.filterOnResumed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsExtraHandler<E : ReduxEvent>(
        private val owner: LifecycleOwner,
        private val errorHandler: (Throwable) -> Unit) : ExtraHandler {

    private val eventSubject = PublishSubject.create<E>()

    val events = eventSubject.hide()!!

    protected fun emitAction(action: E) {
        eventSubject.onNext(action)
    }

    override fun subscribe(source: Observable<ReduxEvent.Extra>) {
        source.filterOnResumed(owner).subscribe(this::onExtraEvent, errorHandler)
    }
}