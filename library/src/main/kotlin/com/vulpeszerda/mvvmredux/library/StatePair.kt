package com.vulpeszerda.mvvmredux.library

/**
 * Created by vulpes on 2017. 9. 18..
 */
data class StatePair<out T>(val prev: T?, val curr: T?)
