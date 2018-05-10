package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.CallSuper
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
    contextService: ContextService
) : ReduxNavigator,
    ContextService by contextService {

    private val eventSubject = PublishSubject.create<ReduxEvent>()

    override val events = eventSubject.hide()!!

    init {
        @Suppress("LeakingThis")
        contextService.owner.lifecycle.addObserver(this)
    }

    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onLifecycleEvent(owner: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            owner.lifecycle.removeObserver(this)
        }
    }

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