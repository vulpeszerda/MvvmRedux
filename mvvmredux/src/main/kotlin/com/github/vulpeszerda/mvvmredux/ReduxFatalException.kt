package com.github.vulpeszerda.mvvmredux

class ReduxFatalException(
    cause: Throwable,
    tag: String? = null
) : Exception("Redux framework crashed!" + if (tag != null) " ($tag)" else "", cause)