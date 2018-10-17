package com.github.vulpeszerda.mvvmredux

import android.util.Log
import io.reactivex.Scheduler

class ReduxStore<T>(
    private val tag: String = TAG,
    initialState: T,
    scheduler: Scheduler,
    private val printLog: Boolean,
    private val onStateUpdated: (T) -> Unit,
    private val onError: (Throwable) -> Unit
) {

    private var latest: T = initialState
    private val worker = scheduler.createWorker()

    fun getState(block: (T) -> Unit) {
        worker.schedule {
            try {
                block(latest)
            } catch (throwable: Throwable) {
                onError(throwable)
            }
        }
    }

    fun setState(block: T.() -> T) {
        worker.schedule {
            try {
                if (printLog) Log.d(tag, "action: $block")
                val oldState = latest
                var newState = oldState
                newState = block(newState)
                if (printLog) Log.d(tag, "state: $newState")
                if (oldState !== newState) {
                    latest = newState
                    onStateUpdated(newState)
                }
            } catch (throwable: Throwable) {
                onError(throwable)
            }
        }
    }

    fun dispose() {
        worker.dispose()
    }

    companion object {
        private const val TAG = "StateStore"
    }

}