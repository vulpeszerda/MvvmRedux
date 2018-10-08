package com.github.vulpeszerda.mvvmreduxsample

data class GlobalState<out T>(val subState: T)