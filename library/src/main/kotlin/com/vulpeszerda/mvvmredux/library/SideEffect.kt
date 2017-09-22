package com.vulpeszerda.mvvmredux.library

/**
 * Created by vulpes on 2017. 8. 29..
 */
interface SideEffect {
    interface State : SideEffect
    interface Navigation : SideEffect
    interface Extra : SideEffect
    data class Error(val throwable: Throwable, val tag: String? = null) : SideEffect
}