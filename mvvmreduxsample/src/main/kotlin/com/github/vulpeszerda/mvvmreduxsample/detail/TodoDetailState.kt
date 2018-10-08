package com.github.vulpeszerda.mvvmreduxsample.detail

import com.github.vulpeszerda.mvvmreduxsample.model.Todo

data class TodoDetailState(
    val todoUid: Long,
    val todo: Todo? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)
