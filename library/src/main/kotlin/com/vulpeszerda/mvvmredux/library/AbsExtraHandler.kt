package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.library.addon.filterOnResumed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsExtraHandler<E : UiEvent>(
        private val owner: LifecycleOwner,
        private val errorHandler: (Throwable) -> Unit) : ExtraHandler {

    private val eventSubject = PublishSubject.create<E>()

    val events = eventSubject.hide()!!

    protected fun emitUiEvent(uiEvent: E) {
        eventSubject.onNext(uiEvent)
    }

    override fun subscribe(source: Observable<SideEffect.Extra>) {
        source.filterOnResumed(owner).subscribe(this::onExtraSideEffect, errorHandler)
    }
}