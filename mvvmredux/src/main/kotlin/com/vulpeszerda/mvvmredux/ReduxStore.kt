package com.vulpeszerda.mvvmredux

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by vulpes on 2017. 8. 29..
 */

class ReduxStore<A, T>(
        initialState: T,
        private val reducer: (T, ReduxEvent.State) -> T,
        private val scheduler: Scheduler,
        private val eventTransformer: (A, () -> T) -> Observable<ReduxEvent.State>) {

    var latest: T = initialState

    fun toState(actions: Observable<A>): Observable<T> {
        return actions
                .flatMap { eventTransformer.invoke(it, { latest }) }
                .observeOn(scheduler)
                .concatMap { action ->
                    Log.d(TAG, "action: $action")
                    val oldState = latest
                    var newState = oldState
                    newState = reducer.invoke(newState, action)
                    Log.d(TAG, "state: $newState")
                    return@concatMap if (oldState !== newState) {
                        latest = newState
                        Observable.just(newState)
                    } else {
                        Observable.empty()
                    }
                }
    }

    companion object {
        private const val TAG = "StateStore"
    }

}