package com.github.vulpeszerda.mvvmreduxsample.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmreduxsample.BaseViewModel
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase
import com.github.vulpeszerda.mvvmreduxsample.model.Todo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TodoCreateViewModel(
    initialState: GlobalState<TodoCreateState>,
    private val getExtraHandler: () -> TodoCreateExtraHandler,
    private val database: TodoDatabase
) : BaseViewModel<TodoCreateState>("TodoCreateVM", initialState) {

    fun save(title: String, message: String) {
        getSubState { state ->
            if (state.loading) return@getSubState
            Single
                .fromCallable {
                    val todo = Todo.create(title, message, false)
                    database.todoDao().insert(todo).firstOrNull()
                        ?: throw IllegalAccessException("Failed to createDiffCompletable todo")
                }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { setSubState { copy(loading = true) } }
                .doFinally { setSubState { copy(loading = false) } }
                .subscribe(
                    { _ ->
                        getExtraHandler().showFinishToast()
                        getExtraHandler().navigateFinish()
                    },
                    { throwable -> getExtraHandler().error(throwable) })
                .addToViewModel()
        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun createFactory(
            initialState: GlobalState<TodoCreateState>,
            getExtraHandler: () -> TodoCreateExtraHandler,
            database: TodoDatabase
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
                    modelClass.isAssignableFrom(TodoCreateViewModel::class.java) ->
                        TodoCreateViewModel(initialState, getExtraHandler, database) as T
                    else -> throw IllegalArgumentException()
                }

            }
    }
}