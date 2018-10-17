package com.github.vulpeszerda.mvvmreduxsample.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmreduxsample.BaseViewModel
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase
import com.github.vulpeszerda.mvvmreduxsample.model.Todo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TodoListViewModel(
    initialState: GlobalState<TodoListState>,
    private val getExtraHandler: () -> TodoListExtraHandler,
    private val database: TodoDatabase
) : BaseViewModel<TodoListState>("TodoListVM", initialState) {

    fun refresh() {
        getSubState { state ->
            if (state.loading) return@getSubState
            Single.fromCallable { database.todoDao().all() }
                .subscribeOn(Schedulers.io())
                .toObservable()
                .doOnSubscribe { setSubState { copy(loading = true) } }
                .doFinally { setSubState { copy(loading = false) } }
                .subscribe({ list ->
                    setSubState { copy(todos = list, error = null) }
                }, { throwable ->
                    setSubState { copy(error = throwable) }
                })
                .addToViewModel()
        }
    }

    fun clearAll() {
        getSubState { state ->
            if (state.loading) return@getSubState
            Completable.fromAction { database.todoDao().deleteAll() }
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { setSubState { copy(loading = true) } }
                .doFinally { setSubState { copy(loading = false) } }
                .subscribe({
                    setSubState { copy(todos = emptyList(), error = null) }
                    getExtraHandler().showClearedToast()
                }, { throwable ->
                    setSubState { copy(error = throwable) }
                })
                .addToViewModel()
        }
    }

    fun check(uid: Long, checked: Boolean) {
        getSubState { state ->
            if (state.loading) return@getSubState
            val todo = state.todos.firstOrNull { it.uid == uid } ?: return@getSubState
            Completable
                .fromAction {
                    database.todoDao().update(Todo().apply {
                        this.uid = todo.uid
                        this.title = todo.title
                        this.message = todo.message
                        this.isCompleted = checked
                        this.createdAt = todo.createdAt
                    })
                }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    setSubState {
                        val matchedIdx = todos.indexOfFirst { it.uid == uid }
                        val checkedTodo = todos.getOrNull(matchedIdx) ?: return@setSubState this
                        copy(todos = todos.toMutableList()
                            .apply {
                                set(matchedIdx, Todo().apply {
                                    this.uid = checkedTodo.uid
                                    this.title = checkedTodo.title
                                    this.message = checkedTodo.message
                                    this.isCompleted = checked
                                    this.createdAt = checkedTodo.createdAt
                                })
                            }
                            .toList())
                    }
                }, { throwable ->
                    getExtraHandler().error(throwable)
                })
                .addToViewModel()
        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun createFactory(
            initialState: GlobalState<TodoListState>,
            getExtraHandler: () -> TodoListExtraHandler,
            database: TodoDatabase
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
                    modelClass.isAssignableFrom(TodoListViewModel::class.java) ->
                        TodoListViewModel(initialState, getExtraHandler, database) as T
                    else -> throw IllegalArgumentException()
                }

            }
    }

}