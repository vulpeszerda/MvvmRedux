package com.github.vulpeszerda.mvvmredux

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.github.vulpeszerda.mvvmredux.addon.bufferUntilOnResumed
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

typealias Action = () -> Unit

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class AbsReduxExtraHandler(
    protected val tag: String,
    contextDelegate: ContextDelegate
) : ReduxComponent.Impl(contextDelegate) {

    private val source = PublishSubject.create<Action>()

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        source.bufferUntilOnResumed(owner)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { available }
            .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
            .subscribe({ it() }) { ReduxFramework.onFatalError(it, tag) }
    }

    fun ensureResumed(action: Action) {
        source.onNext(action)
    }
}