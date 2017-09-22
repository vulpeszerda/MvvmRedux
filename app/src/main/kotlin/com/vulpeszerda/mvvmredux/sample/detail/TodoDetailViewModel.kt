package com.vulpeszerda.mvvmredux.sample.detail

import com.vulpeszerda.mvvmredux.library.BaseViewModel
import com.vulpeszerda.mvvmredux.library.GlobalState
import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 9. 22..
 */
class TodoDetailViewModel(private val database: TodoDatabase) :
        BaseViewModel<TodoDetailUiEvent, TodoDetailState>() {

    private val blockingActionSubject = PublishSubject.create<TodoDetailUiEvent>()

    override fun toSideEffect(uiEvents: Flowable<TodoDetailUiEvent>): Observable<SideEffect> {
        return Observable.merge(
                uiEvents.toObservable()
                        .flatMap {
                            when (it) {
                                is TodoDetailUiEvent.CheckTodo,
                                is TodoDetailUiEvent.Refresh -> {
                                    blockingActionSubject.onNext(it)
                                    Observable.empty()
                                }
                                else -> handleUiEvent(it)
                            }
                        },
                blockingActionSubject
                        .toFlowable(BackpressureStrategy.DROP)
                        .flatMap({
                            handleUiEvent(it).toFlowable(BackpressureStrategy.ERROR)
                        }, 1)
                        .toObservable())
    }

    private fun handleUiEvent(uiEvent: TodoDetailUiEvent): Observable<SideEffect> =
            when (uiEvent) {
                is TodoDetailUiEvent.Refresh -> refresh(uiEvent.todoUid, uiEvent.silent)
                is TodoDetailUiEvent.CheckTodo -> check(uiEvent.todoUid, uiEvent.checked)
            }

    private fun refresh(todoUid: Long, silent: Boolean): Observable<SideEffect> {
        return Maybe.fromCallable { database.todoDao().findByUid(todoUid) }
                .subscribeOn(Schedulers.io())
                .toSingle()
                .flatMapObservable<SideEffect> { todo ->
                    Observable.fromArray(
                            TodoDetailSideEffect.SetTodo(todo),
                            TodoDetailSideEffect.SetError(null))
                }
                .onErrorReturn { TodoDetailSideEffect.SetError(it) }
                .let {
                    if (silent) it else it
                            .startWith(TodoDetailSideEffect.SetLoading(true))
                            .concatWith(Observable.just(TodoDetailSideEffect.SetLoading(false)))
                }
    }

    private fun check(todoUid: Long, checked: Boolean): Observable<SideEffect> {
        return Maybe.fromCallable { database.todoDao().findByUid(todoUid) }
                .flatMapSingleElement { todo ->
                    val changed = todo.apply { isCompleted = checked }
                    Completable.fromAction { database.todoDao().update(changed) }
                            .toSingleDefault(changed)
                }
                .subscribeOn(Schedulers.io())
                .toSingle()
                .flatMapObservable<SideEffect> { todo ->
                    Observable.fromArray(
                            TodoDetailSideEffect.SetTodo(todo),
                            TodoDetailSideEffect.SetError(null),
                            TodoDetailSideEffect.ShowCheckedToast(checked))
                }
                .onErrorReturn { TodoDetailSideEffect.SetError(it) }
                .startWith(TodoDetailSideEffect.SetLoading(true))
                .concatWith(Observable.just(TodoDetailSideEffect.SetLoading(false)))
    }

    override fun reduceState(state: GlobalState<TodoDetailState>,
                             action: SideEffect.State): GlobalState<TodoDetailState> {
        var newState = super.reduceState(state, action)
        var subState = newState.subState
        when (action) {
            is TodoDetailSideEffect.SetLoading ->
                subState = subState.copy(loading = action.loading)
            is TodoDetailSideEffect.SetError ->
                subState = subState.copy(error = action.error)
            is TodoDetailSideEffect.SetTodo -> {
                subState = subState.copy(todo = action.todo)
            }
        }
        if (subState !== newState.subState) {
            newState = newState.copy(subState = subState)
        }
        return newState
    }

}