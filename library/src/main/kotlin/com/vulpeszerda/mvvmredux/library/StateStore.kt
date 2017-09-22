package com.vulpeszerda.mvvmredux.library

import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Maybe
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 29..
 */

class StateStore<T>(initialState: T, private val errorHandler: (Throwable) -> Unit) {

    private val stateSubject = BehaviorSubject.createDefault(initialState)
    private val actionSubject = PublishSubject.create<SideEffect.State>()
    private val reducers = ArrayList<(T, SideEffect.State) -> T>()
    private val disposable: Disposable

    val state = stateSubject.hide()
    val latest: T = stateSubject.value

    init {
        disposable = actionSubject
                .toFlowable(BackpressureStrategy.BUFFER)
                .concatMap { action ->
                    Log.d(TAG, "action: $action")
                    Maybe.fromCallable<T> {
                        val oldState = stateSubject.value
                        var newState = oldState
                        synchronized(this@StateStore) {
                            reducers.forEach { newState = it.invoke(newState, action) }
                        }
                        Log.d(TAG, "state: $newState")
                        if (oldState !== newState) {
                            return@fromCallable newState
                        } else {
                            return@fromCallable null
                        }
                    }.onErrorResumeNext(Function<Throwable, Maybe<T>> { throwable ->
                        errorHandler.invoke(throwable)
                        Maybe.empty<T>()
                    }).subscribeOn(Schedulers.computation()).toFlowable()
                }
                .subscribe(stateSubject::onNext, errorHandler::invoke)
    }

    @Synchronized
    fun addReducer(reducer: (T, SideEffect.State) -> T) {
        if (reducers.indexOf(reducer) < 0) {
            reducers.add(reducer)
        }
    }

    @Synchronized
    fun removeReducer(reducer: (T, SideEffect.State) -> T) {
        reducers.remove(reducer)
    }

    @Synchronized
    fun dispatch(action: SideEffect.State) {
        actionSubject.onNext(action)
    }

    fun terminate() {
        disposable.dispose()
    }

    companion object {
        private const val TAG = "StateStore"
    }

}