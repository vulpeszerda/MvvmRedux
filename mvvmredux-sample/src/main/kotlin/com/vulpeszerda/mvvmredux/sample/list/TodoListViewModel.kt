package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.AbsReduxViewModel
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.GlobalState
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import com.vulpeszerda.mvvmredux.sample.model.Todo
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class TodoListViewModel(private val database: TodoDatabase) :
    AbsReduxViewModel<GlobalState<TodoListState>>() {

    private val blockingActionSubject = PublishSubject.create<ReduxEvent>()

    override fun eventTransformer(
        events: Observable<ReduxEvent>,
        getState: () -> GlobalState<TodoListState>
    ):
            Observable<ReduxEvent> {
        return blockingActionSubject
            .toFlowable(BackpressureStrategy.DROP)
            .flatMap({
                handleEvent(it, getState).toFlowable(BackpressureStrategy.ERROR)
            }, 1)
            .toObservable()
            .mergeWith(super.eventTransformer(events, getState)
                .flatMap {
                    when (it) {
                        is TodoListEvent.Refresh,
                        is TodoListEvent.ShowClearConfirm -> {
                            blockingActionSubject.onNext(it)
                            Observable.empty()
                        }
                        else -> handleEvent(it, getState)
                    }
                })
    }

    private fun handleEvent(
        event: ReduxEvent,
        getState: () -> GlobalState<TodoListState>
    ): Observable<ReduxEvent> {
        return Observable.just<ReduxEvent>(
            when (event) {
                is TodoListEvent.Refresh -> return refresh(event.silent)
                is TodoListEvent.ConfirmClearAll -> return clearAll()
                is TodoListEvent.CheckTodo -> return check(event.uid, event.checked, getState)
                else -> event
            }
        )
    }

    private fun refresh(silent: Boolean): Observable<ReduxEvent> {
        return Single.fromCallable { database.todoDao().all() }
            .subscribeOn(Schedulers.io())
            .flatMapObservable<ReduxEvent> { list ->
                Observable.fromArray(
                    TodoListEvent.SetTodos(list),
                    TodoListEvent.SetError(null)
                )
            }
            .onErrorReturn { TodoListEvent.SetError(it) }
            .let {
                if (silent) it else it
                    .startWith(TodoListEvent.SetLoading(true))
                    .concatWith(Observable.just(TodoListEvent.SetLoading(false)))
            }
    }

    private fun clearAll(): Observable<ReduxEvent> {
        return Completable.fromAction { database.todoDao().deleteAll() }
            .subscribeOn(Schedulers.io())
            .andThen(
                Observable.fromArray<ReduxEvent>(
                    TodoListEvent.SetLoading(false),
                    TodoListEvent.SetTodos(ArrayList()),
                    TodoListEvent.SetError(null),
                    TodoListEvent.ShowClearedToast()
                )
            )
            .onErrorResumeNext { throwable: Throwable ->
                Observable.fromArray(
                    TodoListEvent.SetLoading(false),
                    TodoListEvent.SetError(throwable)
                )
            }
            .startWith(TodoListEvent.SetLoading(true))
    }

    private fun check(uid: Long, checked: Boolean, getState: () -> GlobalState<TodoListState>):
            Observable<ReduxEvent> {
        return Completable
            .fromAction {
                val todo = getState.invoke().subState.todos.firstOrNull { it.uid == uid }
                        ?: return@fromAction
                database.todoDao().update(Todo().apply {
                    this.uid = todo.uid
                    title = todo.title
                    message = todo.message
                    isCompleted = checked
                    createdAt = todo.createdAt
                })
            }
            .subscribeOn(Schedulers.io())
            .toObservable<ReduxEvent>()
            .startWith(TodoListEvent.CheckTodo(uid, checked))
    }

    override fun reduceState(
        state: GlobalState<TodoListState>,
        event: ReduxEvent.State
    ): GlobalState<TodoListState> {
        var newState = super.reduceState(state, event)
        var subState = newState.subState
        when (event) {
            is TodoListEvent.SetLoading ->
                subState = subState.copy(loading = event.loading)
            is TodoListEvent.SetError ->
                subState = subState.copy(error = event.error)
            is TodoListEvent.CheckTodo -> {
                val matchedIdx = subState.todos.indexOfFirst { it.uid == event.uid }
                val todo = subState.todos.getOrNull(matchedIdx)
                if (todo != null) {
                    subState = subState.copy(todos = subState.todos.toMutableList()
                        .apply {
                            set(matchedIdx, Todo().apply {
                                uid = todo.uid
                                title = todo.title
                                message = todo.message
                                isCompleted = event.checked
                                createdAt = todo.createdAt
                            })
                        }
                        .toList())
                }
            }
            is TodoListEvent.SetTodos ->
                subState = subState.copy(todos = event.todos)
        }
        if (subState !== newState.subState) {
            newState = newState.copy(subState = subState)
        }
        return newState
    }
}