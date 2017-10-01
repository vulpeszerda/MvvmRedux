package com.vulpeszerda.mvvmredux.sample

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.vulpeszerda.mvvmredux.sample.create.TodoCreateViewModel
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import com.vulpeszerda.mvvmredux.sample.detail.TodoDetailViewModel
import com.vulpeszerda.mvvmredux.sample.list.TodoListViewModel

/**
 * Created by vulpes on 2017. 9. 5..
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val database: TodoDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            return TodoListViewModel(database) as T
        } else if (modelClass.isAssignableFrom(TodoCreateViewModel::class.java)) {
            return TodoCreateViewModel(database) as T
        } else if (modelClass.isAssignableFrom(TodoDetailViewModel::class.java)) {
            return TodoDetailViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class : $modelClass")
    }

}