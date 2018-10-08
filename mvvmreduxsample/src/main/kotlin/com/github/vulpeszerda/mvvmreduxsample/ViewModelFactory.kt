package com.github.vulpeszerda.mvvmreduxsample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmreduxsample.create.TodoCreateViewModel
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase
import com.github.vulpeszerda.mvvmreduxsample.detail.TodoDetailViewModel
import com.github.vulpeszerda.mvvmreduxsample.list.TodoListViewModel

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