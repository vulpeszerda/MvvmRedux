package com.github.vulpeszerda.mvvmreduxsample

class ReduxFatalException(
    cause: Throwable,
    tag: String? = null
) : Exception("Redux framework crashed!" + if (tag != null) " ($tag)" else "", cause)