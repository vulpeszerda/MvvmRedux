package com.github.vulpeszerda.mvvmreduxsample.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.vulpeszerda.mvvmreduxsample.BaseViewModel
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class TodoDetailViewModel(
    initialState: GlobalState<TodoDetailState>,
    private val getExtraHandler: () -> TodoDetailExtraHandler,
    private val database: TodoDatabase
) : BaseViewModel<TodoDetailState>("TodoDetailVM", initialState) {

    fun refresh() {
        getSubState { state ->
            if (state.loading) return@getSubState
            Maybe.fromCallable { database.todoDao().findByUid(state.todoUid) }
                .subscribeOn(Schedulers.io())
                .toSingle()
                .doOnSubscribe { setSubState { copy(loading = true) } }
                .doFinally { setSubState { copy(loading = false) } }
                .subscribe(
                    { todo -> setSubState { copy(todo = todo, error = null) } },
                    { throwable -> setSubState { copy(error = throwable) } })
                .addToViewModel()
        }
    }

    private fun check(todoUid: Long, checked: Boolean) {
        getSubState { state ->
            if (state.loading) return@getSubState
            Maybe.fromCallable { database.todoDao().findByUid(todoUid) }
                .flatMapSingleElement { todo ->
                    val changed = todo.apply { isCompleted = checked }
                    Completable.fromAction { database.todoDao().update(changed) }
                        .toSingleDefault(changed)
                }
                .subscribeOn(Schedulers.io())
                .toSingle()
                .doOnSubscribe { setSubState { copy(loading = true) } }
                .doFinally { setSubState { copy(loading = false) } }
                .subscribe(
                    { todo ->
                        setSubState { copy(todo = todo, error = null) }
                        getExtraHandler().showCheckedToast(checked)
                    },
                    { throwable -> setSubState { copy(error = throwable) } })
                .addToViewModel()
        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun createFactory(
            initialState: GlobalState<TodoDetailState>,
            getExtraHandler: () -> TodoDetailExtraHandler,
            database: TodoDatabase
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
                    modelClass.isAssignableFrom(TodoDetailViewModel::class.java) ->
                        TodoDetailViewModel(initialState, getExtraHandler, database) as T
                    else -> throw IllegalArgumentException()
                }

            }
    }
}