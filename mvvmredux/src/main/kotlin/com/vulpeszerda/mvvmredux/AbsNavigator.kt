package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.addon.filterOnResumed
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsNavigator<E : ReduxEvent>(
        private val owner: LifecycleOwner,
        private val errorHandler: (Throwable) -> Unit) : Navigator {

    private val eventSubject = PublishSubject.create<E>()

    val events = eventSubject.hide()!!

    protected fun emitAction(action: E) {
        eventSubject.onNext(action)
    }

    override fun subscribe(source: Observable<ReduxEvent.Navigation>): Disposable =
            source.filterOnResumed(owner).subscribe(this::navigate, errorHandler)
}