package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.sample.model.Todo

/**
 * Created by vulpes on 2017. 9. 22..
 */
data class TodoDetailState(
    val todoUid: Long,
    val todo: Todo? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)
