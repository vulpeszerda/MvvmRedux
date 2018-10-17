package com.github.vulpeszerda.mvvmredux

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

@Suppress("MemberVisibilityCanBePrivate")
abstract class ReduxViewModel<T>(
    private val tag: String,
    initialState: T,
    reducerScheduler: Scheduler = AndroidSchedulers.mainThread(),
    printLog: Boolean = false
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val stateSubject = BehaviorSubject.create<T>()

    val state: Observable<T> = stateSubject.hide()

    val stateStore: ReduxStore<T> = ReduxStore(
        tag,
        initialState,
        reducerScheduler,
        printLog,
        stateSubject::onNext
    ) { ReduxFramework.onFatalError(it, tag) }

    protected fun addDisposable(disposable: Disposable) {
        this.disposable.add(disposable)
    }

    protected fun removeDisposable(disposable: Disposable) {
        this.disposable.remove(disposable)
    }

    fun Disposable.addToViewModel() {
        addDisposable(this)
    }

    override fun onCleared() {
        disposable.clear()
        stateStore.dispose()
        super.onCleared()
    }

    fun setState(block: T.() -> T) {
        stateStore.setState(block)
    }

    fun getState(block: (T) -> Unit) {
        stateStore.getState(block)
    }
}
