package com.github.vulpeszerda.mvvmreduxsample

object ReduxFramework {

    interface FatalErrorHandler {

        fun accept(throwable: Throwable)
    }

    @JvmStatic
    @Volatile
    var fatalHandler: FatalErrorHandler? = null

    @JvmStatic
    fun onFatalError(throwable: Throwable, tag: String? = null) {
        val error = throwable as? ReduxFatalException ?: ReduxFatalException(throwable, tag)
        val errorHandler = fatalHandler
        if (errorHandler != null) {
            try {
                errorHandler.accept(error)
            } catch (e: Throwable) {
                e.printStackTrace()
                uncaught(e)
            }
            return
        }
        error.printStackTrace()
        uncaught(error)
    }

    private fun uncaught(throwable: Throwable) {
        val currentThread = Thread.currentThread()
        currentThread.uncaughtExceptionHandler?.uncaughtException(currentThread, throwable)
    }

}