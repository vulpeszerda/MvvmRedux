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

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoCreateViewModel : BaseViewModel<TodoCreateUiEvent, TodoCreateState>() {

    private var database: TodoDatabase? = null

    fun initialize(delegate: TodoCreateViewModelDelegate,
                   initialState: GlobalState<TodoCreateState>,
                   database: TodoDatabase) {
        initialize(delegate, initialState)
        this.database = database
    }

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
                    database?.todoDao()?.insert(todo)?.firstOrNull() ?:
                            throw IllegalAccessException("Failed to create todo")
                }
                .toObservable()
                .flatMap<SideEffect> {
                    Observable.fromArray(TodoCreateSideEffect.SetLoading(false),
                            TodoCreateSideEffect.ShowFinishToast(),
                            TodoCreateSideEffect.NavigateFinish())
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.fromArray(TodoCreateSideEffect.SetLoading(false),
                            SideEffect.Error(throwable, "save"))
                }
                .startWith(TodoCreateSideEffect.SetLoading(true))
    }
}