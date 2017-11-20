package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 25..
 */
abstract class ReduxViewModel<T>(private val printLog: Boolean = false) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val errorSubject = PublishSubject.create<ReduxEvent.Error>()
    private val navigationSubject = PublishSubject.create<ReduxEvent.Navigation>()
    private val extraSubject = PublishSubject.create<ReduxEvent.Extra>()
    private val stateSubject = BehaviorSubject.create<T>()

    private var retryCount = 0

    val error: Observable<ReduxEvent.Error> = errorSubject.hide()
    val extra: Observable<ReduxEvent.Extra> = extraSubject.hide()
    val navigation: Observable<ReduxEvent.Navigation> = navigationSubject.hide()
    val state: Observable<T> = stateSubject.hide()

    var stateStore: ReduxStore<T>? = null
        private set

    fun initialize(initialState: T, events: Observable<ReduxEvent>) {
        disposable.clear()
        stateStore = ReduxStore(initialState,
                this::reduceState,
                Schedulers.computation(), { actions, getState ->
            eventTransformer(actions, getState)
                    .filter {
                        when (it) {
                            is ReduxEvent.Navigation ->
                                navigationSubject.onNext(it)
                            is ReduxEvent.Extra ->
                                extraSubject.onNext(it)
                            is ReduxEvent.Error ->
                                errorSubject.onNext(it)
                            else -> return@filter true
                        }
                        return@filter false
                    }
                    .map { it as ReduxEvent.State }
        }, printLog)
        addDisposable(stateStore!!.toState(events)
                .retry { throwable ->
                    errorSubject.onNext(ReduxEvent.Error(throwable))
                    return@retry ++retryCount < MAX_RETRY_COUNT
                }
                .subscribe(stateSubject::onNext) { throwable ->
                    errorSubject.onNext(ReduxEvent.Error(throwable))
                })
    }

    protected open fun eventTransformer(events: Observable<ReduxEvent>, getState: () -> T):
            Observable<ReduxEvent> {
        return events
    }

    protected open fun reduceState(state: T, event: ReduxEvent.State): T {
        return state
    }

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

    companion object {

        private const val MAX_RETRY_COUNT = 5
    }
}