package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 25..
 */
abstract class BaseViewModel<E : UiEvent, T> : ViewModel() {

    private val disposable = CompositeDisposable()
    private val errorSubject = PublishSubject.create<SideEffect.Error>()
    private val navigationSubject = PublishSubject.create<SideEffect.Navigation>()
    private val extraSubject = PublishSubject.create<SideEffect.Extra>()
    private val stateSubject = BehaviorSubject.create<GlobalState<T>>()

    val error: Observable<SideEffect.Error> = errorSubject.hide()
    val extra: Observable<SideEffect.Extra> = extraSubject.hide()
    val navigation: Observable<SideEffect.Navigation> = navigationSubject.hide()
    val state: Observable<GlobalState<T>> = stateSubject.hide()

    var stateStore: StateStore<GlobalState<T>>? = null
        private set

    fun initialize(initialState: GlobalState<T>, events: Flowable<E>) {
        disposable.clear()
        stateStore?.apply {
            removeReducer(this@BaseViewModel::reduceState)
            terminate()
        }

        stateStore = StateStore(initialState,
                { throwable -> errorSubject.onNext(SideEffect.Error(throwable)) })
                .apply { addReducer(this@BaseViewModel::reduceState) }
        addDisposable(stateStore!!.state.subscribe(stateSubject::onNext, { throwable ->
            errorSubject.onNext(SideEffect.Error(throwable))
        }))
        addDisposable(toSideEffect(events)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    when (it) {
                        is SideEffect.State ->
                            stateStore?.dispatch(it)
                        is SideEffect.Navigation ->
                            navigationSubject.onNext(it)
                        is SideEffect.Extra ->
                            extraSubject.onNext(it)
                        is SideEffect.Error ->
                            errorSubject.onNext(it)
                    }
                }
                .retry { throwable ->
                    errorSubject.onNext(SideEffect.Error(throwable))
                    return@retry true
                }
                .subscribe())
    }

    protected open fun toSideEffect(uiEvents: Flowable<E>): Observable<SideEffect> {
        return Observable.never()
    }

    protected open fun reduceState(state: GlobalState<T>,
                                   action: SideEffect.State): GlobalState<T> {
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
        stateStore?.apply {
            removeReducer(this@BaseViewModel::reduceState)
            terminate()
        }
        super.onCleared()
    }
}