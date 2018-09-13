package com.vulpeszerda.mvvmredux.sample

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.vulpeszerda.mvvmredux.sample.create.TodoCreateViewModel
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import com.vulpeszerda.mvvmredux.sample.detail.TodoDetailViewModel
import com.vulpeszerda.mvvmredux.sample.list.TodoListViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val database: TodoDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(TodoListViewModel::class.java) ->
            TodoListViewModel(database) as T
        modelClass.isAssignableFrom(TodoCreateViewModel::class.java) ->
            TodoCreateViewModel(database) as T
        modelClass.isAssignableFrom(TodoDetailViewModel::class.java) ->
            TodoDetailViewModel(database) as T
        else -> throw IllegalArgumentException("Unknown viewmodel class : $modelClass")
    }

}