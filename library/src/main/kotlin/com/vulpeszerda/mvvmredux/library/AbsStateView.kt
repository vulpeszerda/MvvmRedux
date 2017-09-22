package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.library.addon.filterOnStarted
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsStateView<T, E : UiEvent>(
        private val owner: LifecycleOwner,
        private val errorHandler: (Throwable) -> Unit) : LayoutContainer, LifecycleObserver {

    private val eventSubject = PublishSubject.create<E>()

    open val events = eventSubject.hide()!!

    protected fun emitUiEvent(uiEvent: E) {
        eventSubject.onNext(uiEvent)
    }

    protected abstract fun onStateChanged(prev: GlobalState<T>?, curr: GlobalState<T>?)

    fun subscribe(source: Observable<GlobalState<T>>): Disposable =
            source.filterOnStarted(owner)
                    .distinctUntilChanged()
                    .scan(StatePair<GlobalState<T>>(null, null))
                    { prevPair, curr -> StatePair(prevPair.curr, curr) }
                    .filter { (prev, curr) -> prev !== curr }
                    .subscribe({ (prev, curr) -> onStateChanged(prev, curr) }, errorHandler)
}