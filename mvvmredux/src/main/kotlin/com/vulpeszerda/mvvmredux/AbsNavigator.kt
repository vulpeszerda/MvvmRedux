package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.addon.filterOnResumed
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsNavigator(
        private val tag: String,
        private val owner: LifecycleOwner) :
        Navigator {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    val events = eventSubject.hide()!!

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }

    override fun subscribe(source: Observable<ReduxEvent.Navigation>): Disposable =
            source.filterOnResumed(owner)
                    .subscribe(this::navigate) {
                        publishEvent(ReduxEvent.Error(it, tag))
                    }
}