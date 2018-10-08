package com.github.vulpeszerda.mvvmredux.sample.list

import com.github.vulpeszerda.mvvmredux.sample.model.Todo

data class TodoListState(
    val todos: List<Todo> = ArrayList(),
    val loading: Boolean = false,
    val error: Throwable? = null
)