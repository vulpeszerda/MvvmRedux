package com.vulpeszerda.mvvmredux.library

/**
 * Created by vulpes on 2017. 8. 25..
 */
data class GlobalState<out T>(val subState: T)