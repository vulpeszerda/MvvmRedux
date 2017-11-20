package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.LifecycleOwner
import com.vulpeszerda.mvvmredux.addon.filterOnResumed
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsExtraHandler(
        private val tag: String,
        private val owner: LifecycleOwner) : ExtraHandler {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    override val events = eventSubject.hide()!!

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }

    override fun subscribe(source: Observable<ReduxEvent.Extra>) {
        source.filterOnResumed(owner).subscribe(this::onExtraEvent) {
            publishEvent(ReduxEvent.Error(it, tag))
        }
    }
}