package com.github.vulpeszerda.mvvmreduxsample

import androidx.lifecycle.Lifecycle
import com.github.vulpeszerda.mvvmreduxsample.addon.bufferUntilOnResumed
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class AbsReduxExtraHandler(
    protected val tag: String,
    contextDelegate: ContextDelegate
) : ReduxComponent.Impl(contextDelegate),
    ReduxExtraHandler {

    override fun subscribe(source: Observable<ReduxEvent.Extra>): Disposable =
        source.bufferUntilOnResumed(owner)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { available }
            .bindUntilEvent(owner, Lifecycle.Event.ON_DESTROY)
            .subscribe(this::onExtraEvent) {
                ReduxFramework.onFatalError(it, tag)
            }
}