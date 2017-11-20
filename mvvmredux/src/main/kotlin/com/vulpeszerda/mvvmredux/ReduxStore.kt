package com.vulpeszerda.mvvmredux

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Created by vulpes on 2017. 8. 29..
 */

class ReduxStore<T>(
        private val initialState: T,
        private val reducer: (T, ReduxEvent.State) -> T,
        private val scheduler: Scheduler,
        private val eventTransformer: (Observable<ReduxEvent>, () -> T) -> Observable<ReduxEvent.State>,
        private val printLog: Boolean = false) {

    var latest: T = initialState

    fun toState(actions: Observable<ReduxEvent>): Observable<T> {
        return actions
                .compose { eventTransformer.invoke(it) { latest } }
                .observeOn(scheduler)
                .concatMap { action ->
                    if (printLog) Log.d(TAG, "action: $action")
                    val oldState = latest
                    var newState = oldState
                    newState = reducer.invoke(newState, action)
                    if (printLog) Log.d(TAG, "state: $newState")
                    return@concatMap if (oldState !== newState) {
                        latest = newState
                        Observable.just(newState)
                    } else {
                        Observable.empty()
                    }
                }
                .startWith(initialState)
    }

    companion object {
        private const val TAG = "StateStore"
    }

}