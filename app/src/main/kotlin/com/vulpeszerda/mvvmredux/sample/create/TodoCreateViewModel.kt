package com.vulpeszerda.mvvmredux.sample.create

import com.vulpeszerda.mvvmredux.library.BaseViewModel
import com.vulpeszerda.mvvmredux.library.GlobalState
import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import com.vulpeszerda.mvvmredux.sample.model.Todo
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoCreateViewModel(private val database: TodoDatabase) :
        BaseViewModel<TodoCreateUiEvent, TodoCreateState>() {

    override fun toSideEffect(uiEvents: Flowable<TodoCreateUiEvent>): Observable<SideEffect> {
        return uiEvents
                .onBackpressureDrop()
                .filter { it is TodoCreateUiEvent.Save }
                .flatMap({ event ->
                    val saveEvent = event as TodoCreateUiEvent.Save
                    save(saveEvent.title, saveEvent.message).toFlowable(BackpressureStrategy.ERROR)
                }, 1)
                .toObservable()
    }

    private fun save(title: String, message: String): Observable<SideEffect> {
        return Single
                .fromCallable {
                    val todo = Todo.create(title, message, false)
                    database.todoDao().insert(todo).firstOrNull() ?:
                            throw IllegalAccessException("Failed to create todo")
                }
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap<SideEffect> {
                    Observable.fromArray(
                            TodoCreateSideEffect.ShowFinishToast(),
                            TodoCreateSideEffect.NavigateFinish())
                }
                .onErrorReturn { SideEffect.Error(it, "save") }
                .startWith(TodoCreateSideEffect.SetLoading(true))
                .concatWith(Observable.just(TodoCreateSideEffect.SetLoading(false)))
    }

    override fun reduceState(state: GlobalState<TodoCreateState>,
                             action: SideEffect.State): GlobalState<TodoCreateState> {
        val prevState = super.reduceState(state, action)
        var newState = prevState
        val prevSubState = prevState.subState
        var newSubState = prevSubState
        when (action) {
            is TodoCreateSideEffect.SetLoading ->
                newSubState = newSubState.copy(loading = action.loading)
        }
        if (newSubState !== prevSubState) {
            newState = newState.copy(subState = newSubState)
        }
        return newState
    }
}