package com.github.vulpeszerda.mvvmreduxsample.list

import com.github.vulpeszerda.mvvmreduxsample.model.Todo

data class TodoListState(
    val todos: List<Todo> = ArrayList(),
    val loading: Boolean = false,
    val error: Throwable? = null
)