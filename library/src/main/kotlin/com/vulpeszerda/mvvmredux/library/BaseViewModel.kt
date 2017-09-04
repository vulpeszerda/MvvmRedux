package com.vulpeszerda.mvvmredux.library

import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by vulpes on 2017. 8. 25..
 */
abstract class BaseViewModel<E : UiEvent, T> : ViewModel() {

    private val disposable = CompositeDisposable()

    private var stateStore: StateStore<GlobalState<T>>? = null

    protected fun initialize(delegate: BaseViewModelDelegate<E>,
                             initialState: GlobalState<T>) {

        disposable.clear()
        stateStore?.apply {
            removeReducer(this@BaseViewModel::reduceState)
            terminate()
        }

        stateStore = StateStore(initialState, delegate).apply {
            addReducer(this@BaseViewModel::reduceState)
        }
        addDisposable(toSideEffect(delegate.events)
                .observeOn(delegate.uiScheduler)
                .doOnNext {
                    when (it) {
                        is SideEffect.State ->
                            stateStore?.dispatch(it)
                        is SideEffect.Navigation ->
                            delegate.navigate(it)
                        is SideEffect.Extra ->
                            delegate.handleExtraSideEffect(it)
                        is SideEffect.Error ->
                            delegate.onError(it.throwable, it.tag)
                    }
                }
                .retry { throwable ->
                    delegate.onError(throwable, null)
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