package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import com.vulpeszerda.mvvmredux.addon.bufferUntilOnResumed
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 21..
 */
@Suppress("unused")
abstract class AbsReduxNavigator(
    protected val tag: String,
    contextWrapper: ContextService
) : ReduxNavigator,
    ContextService by contextWrapper {

    constructor(tag: String, activity: ReduxActivity) : this(tag, ActivityContextService(activity))
    constructor(tag: String, fragment: ReduxFragment) : this(tag, FragmentContextService(fragment))

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    override val events = eventSubject.hide()!!

    protected fun publishEvent(event: ReduxEvent) {
        eventSubject.onNext(event)
    }

    override fun subscribe(source: Observable<ReduxEvent.Navigation>): Disposable =
        source.bufferUntilOnResumed(owner)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { available }
            .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
            .subscribe(this::navigate) {
                ReduxFramework.onFatalError(it, tag)
            }
}