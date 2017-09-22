package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.library.addon.bufferUntilOnResumed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsErrorHandler<E : UiEvent>(private val owner: LifecycleOwner) : ErrorHandler {

    private val eventSubject = PublishSubject.create<E>()

    val events = eventSubject.hide()!!

    protected fun emitUiEvent(uiEvent: E) {
        eventSubject.onNext(uiEvent)
    }

    override fun subscribe(source: Observable<SideEffect.Error>) {
        source.bufferUntilOnResumed(owner).subscribe(this::onError) { throwable ->
            onError(SideEffect.Error(throwable))
        }
    }
}