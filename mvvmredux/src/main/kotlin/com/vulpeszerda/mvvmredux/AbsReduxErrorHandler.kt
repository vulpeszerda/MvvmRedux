package com.vulpeszerda.mvvmredux

import com.vulpeszerda.mvvmredux.addon.bufferUntilOnResumed
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
abstract class AbsReduxErrorHandler(
        protected val tag: String,
        contextWrapper: ContextWrapper) :
        ReduxErrorHandler,
        ContextWrapper by contextWrapper {

    constructor(tag: String, activity: ReduxActivity) : this(tag, ActivityContextWrapper(activity))
    constructor(tag: String, fragment: ReduxFragment) : this(tag, FragmentContextWrapper(fragment))

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    override val events = eventSubject.hide()!!

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }

    override fun subscribe(source: Observable<ReduxEvent.Error>): Disposable =
            source.bufferUntilOnResumed(owner).subscribe(this::onError) { throwable ->
                onError(ReduxEvent.Error(throwable, tag))
            }
}