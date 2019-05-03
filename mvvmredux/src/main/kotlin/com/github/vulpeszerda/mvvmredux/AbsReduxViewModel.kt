package com.github.vulpeszerda.mvvmredux

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

@Suppress("MemberVisibilityCanBePrivate")
abstract class AbsReduxViewModel<T>(
    private val tag: String = "ReduxViewModel",
    private val reducerScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val printLog: Boolean = false
) : ViewModel(), ReduxViewModel<T> {

    private val disposable = CompositeDisposable()
    private val extraSubject = PublishSubject.create<ReduxEvent.Extra>()
    private val stateSubject = BehaviorSubject.create<T>()

    override val extra: Observable<ReduxEvent.Extra> = extraSubject.hide()
    override val state: Observable<T> = stateSubject.hide()

    override val stateStore: ReduxStore<T>?
        get() = _stateStore

    private var _stateStore: ReduxStore<T>? = null

    override fun initialize(initialState: T, events: Observable<ReduxEvent>) {
        disposable.clear()
        _stateStore = ReduxStore(
            initialState = initialState,
            reducer = this::reduceState,
            reducerScheduler = reducerScheduler,
            tag = tag,
            printLog = printLog,
            eventTransformer = { actions, getState ->
                eventTransformer(actions, getState)
                    .filter {
                        when (it) {
                            is ReduxEvent.Extra ->
                                extraSubject.onNext(it)
                            else -> return@filter it is ReduxEvent.State
                        }
                        return@filter false
                    }
                    .map { it as ReduxEvent.State }
            })
        addDisposable(
            requireNotNull(stateStore)
                .toState(events)
                .subscribe(stateSubject::onNext) {
                    ReduxFramework.onFatalError(it, tag)
                }
        )
    }

    protected open fun eventTransformer(events: Observable<ReduxEvent>, getState: () -> T):
            Observable<ReduxEvent> = events

    protected open fun reduceState(state: T, event: ReduxEvent.State): T = state

    protected fun addDisposable(disposable: Disposable) {
        this.disposable.add(disposable)
    }

    protected fun removeDisposable(disposable: Disposable) {
        this.disposable.remove(disposable)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}
