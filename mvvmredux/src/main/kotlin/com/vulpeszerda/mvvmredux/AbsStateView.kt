package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.addon.filterOnStarted
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsStateView<T>(
        private val tag: String,
        private val owner: LifecycleOwner) :
        LayoutContainer, LifecycleObserver {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    open val events = eventSubject.hide()!!

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }

    protected abstract fun onStateChanged(prev: T?, curr: T?)

    fun subscribe(source: Observable<T>): Disposable =
            source.filterOnStarted(owner)
                    .distinctUntilChanged()
                    .scan(StatePair<T>(null, null))
                    { prevPair, curr -> StatePair(prevPair.curr, curr) }
                    .filter { (prev, curr) -> prev !== curr }
                    .subscribe({ (prev, curr) -> onStateChanged(prev, curr) }) {
                        publishEvent(ReduxEvent.Error(it, tag))
                    }
}