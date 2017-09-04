package com.vulpeszerda.mvvmredux.library

/**
 * Created by vulpes on 2017. 8. 25..
 */
interface ExceptionHandler {
    fun onError(throwable: Throwable, tag: String?, vararg otherArguments: Any)
}