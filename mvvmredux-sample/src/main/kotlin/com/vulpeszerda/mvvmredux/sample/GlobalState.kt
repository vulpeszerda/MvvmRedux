package com.vulpeszerda.mvvmredux.sample

data class GlobalState<out T>(val subState: T)