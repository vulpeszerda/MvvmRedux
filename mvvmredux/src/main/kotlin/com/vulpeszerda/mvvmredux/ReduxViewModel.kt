package com.vulpeszerda.mvvmredux

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 25..
 */
abstract class ReduxViewModel<T>(
        private val tag: String = "ReduxViewModel",
        private val onFatalErrorHandler: ((Throwable) -> Unit)? = null,
        private val reducerScheduler: Scheduler = AndroidSchedulers.mainThread(),
        private val maxRetryCount: Int = -1,
        private val printLog: Boolean = false) : ViewModel() {

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
        stateStore = ReduxStore(
                initialState = initialState,
                reducer = this::reduceState,
                reducerScheduler = reducerScheduler,
                tag = tag,
                printLog = printLog,
                eventTransformer = { actions, getState ->
                    eventTransformer(actions, getState)
                            .filter {
                                when (it) {
                                    is ReduxEvent.Navigation ->
                                        navigationSubject.onNext(it)
                                    is ReduxEvent.Extra ->
                                        extraSubject.onNext(it)
                                    is ReduxEvent.Error ->
                                        errorSubject.onNext(it)
                                    else -> return@filter it is ReduxEvent.State
                                }
                                return@filter false
                            }
                            .map { it as ReduxEvent.State }
                })
        addDisposable(stateStore!!.toState(events)
                .subscribe(stateSubject::onNext) { throwable ->
                    if (onFatalErrorHandler != null) {
                        onFatalErrorHandler.invoke(throwable)
                    } else {
                        errorSubject.onNext(ReduxEvent.Error(throwable, TAG_FATAL))
                        if (maxRetryCount == -1 || ++retryCount < maxRetryCount) {
                            AndroidSchedulers.mainThread().createWorker().schedule {
                                initialize(stateStore?.latest ?: initialState, events)
                            }
                        }
                    }
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

        @JvmField
        val TAG_FATAL = "FatalError"
    }
}