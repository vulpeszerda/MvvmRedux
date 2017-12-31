package com.vulpeszerda.mvvmredux

/**
 * Created by vulpes on 2017. 12. 29..
 */
class ReduxFatalException(
        cause: Throwable,
        tag: String? = null) :
        Exception("Redux framework crashed!" + if (tag != null) " ($tag)" else "", cause)