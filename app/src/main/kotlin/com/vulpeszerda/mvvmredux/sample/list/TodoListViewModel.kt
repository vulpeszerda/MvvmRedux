package com.vulpeszerda.mvvmredux.sample.list

import com.vulpeszerda.mvvmredux.library.BaseViewModel
import com.vulpeszerda.mvvmredux.library.BaseViewModelDelegate
import com.vulpeszerda.mvvmredux.library.GlobalState
import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import com.vulpeszerda.mvvmredux.sample.model.Todo
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by vulpes on 2017. 8. 30..
 */
class TodoListViewModel : BaseViewModel<TodoListUiEvent, TodoListState>() {

    private val blockingActionSubject = PublishSubject.create<TodoListUiEvent>()
    private var database: TodoDatabase? = null

    fun initialize(delegate: BaseViewModelDelegate<TodoListUiEvent>,
                   initialState: GlobalState<TodoListState>,
                   database: TodoDatabase) {
        initialize(delegate, initialState)
        this.database = database
    }

    override fun toSideEffect(uiEvents: Flowable<TodoListUiEvent>):
            Observable<SideEffect> {
        return Observable.merge(
                uiEvents.toObservable()
                        .flatMap {
                            when (it) {
                                is TodoListUiEvent.Refresh,
                                is TodoListUiEvent.ClickClearAll -> {
                                    blockingActionSubject.onNext(it)
                                    Observable.empty()
                                }
                                else -> handleNonBlockingUiEvent(it)
                            }
                        },
                blockingActionSubject
                        .toFlowable(BackpressureStrategy.DROP)
                        .flatMap({
                            handleNonBlockingUiEvent(it).toFlowable(BackpressureStrategy.ERROR)
                        }, 1)
                        .toObservable())
    }

    private fun handleNonBlockingUiEvent(uiEvent: TodoListUiEvent): Observable<SideEffect> {
        return Observable.just<SideEffect>(when (uiEvent) {
            is TodoListUiEvent.Refresh -> return refresh()
            is TodoListUiEvent.ConfirmClearAll -> return clearAll()
            is TodoListUiEvent.CheckTodo -> return check(uiEvent.todo)
            is TodoListUiEvent.ClickClearAll -> TodoListSideEffect.ShowClearConfirm()
            is TodoListUiEvent.ClickCreate -> TodoListSideEffect.NavigateCreate()
            is TodoListUiEvent.ClickDetail -> TodoListSideEffect.NavigateDetail(uiEvent.uid)
        })
    }

    private fun refresh(): Observable<SideEffect> {
        return Maybe.fromCallable { database?.todoDao()?.all() }
                .subscribeOn(Schedulers.io())
                .flatMapObservable<SideEffect> { list ->
                    Observable.fromArray(
                            TodoListSideEffect.SetLoading(false),
                            TodoListSideEffect.SetTodos(list),
                            TodoListSideEffect.SetError(null))
                }
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.fromArray(
                            TodoListSideEffect.SetLoading(false),
                            TodoListSideEffect.SetError(throwable))
                }
                .startWith(TodoListSideEffect.SetLoading(true))
    }

    private fun clearAll(): Observable<SideEffect> {
        return Completable.fromAction { database?.todoDao()?.deleteAll() }
                .subscribeOn(Schedulers.io())
                .andThen(Observable.fromArray<SideEffect>(
                        TodoListSideEffect.SetLoading(false),
                        TodoListSideEffect.SetTodos(ArrayList()),
                        TodoListSideEffect.SetError(null),
                        TodoListSideEffect.ShowClearedToast()))
                .onErrorResumeNext { throwable: Throwable ->
                    Observable.fromArray(
                            TodoListSideEffect.SetLoading(false),
                            TodoListSideEffect.SetError(throwable))
                }
                .startWith(TodoListSideEffect.SetLoading(true))
    }

    private fun check(todo: Todo): Observable<SideEffect> {
        return Completable
                .fromAction {
                    database?.todoDao()?.update(Todo().apply {
                        uid = todo.uid
                        title = todo.title
                        message = todo.message
                        isCompleted = !todo.isCompleted
                        createdAt = todo.createdAt
                    })
                }
                .subscribeOn(Schedulers.io())
                .toObservable<SideEffect>()
                .startWith(TodoListSideEffect.CheckTodo(todo.uid, !todo.isCompleted))
    }

    override fun reduceState(state: GlobalState<TodoListState>,
                             action: SideEffect.State): GlobalState<TodoListState> {
        var newState = super.reduceState(state, action)
        var subState = newState.subState
        when (action) {
            is TodoListSideEffect.SetLoading ->
                subState = subState.copy(loading = action.loading)
            is TodoListSideEffect.SetError ->
                subState = subState.copy(error = action.error)
            is TodoListSideEffect.CheckTodo -> {
                val matchedIdx = subState.todos.indexOfFirst { it.uid == action.uid }
                val todo = subState.todos.getOrNull(matchedIdx)
                if (todo != null) {
                    subState = subState.copy(todos = subState.todos.toMutableList()
                            .apply {
                                set(matchedIdx, Todo().apply {
                                    uid = todo.uid
                                    title = todo.title
                                    message = todo.message
                                    isCompleted = action.checked
                                    createdAt = todo.createdAt
                                })
                            }
                            .toList())
                }
            }
            is TodoListSideEffect.SetTodos ->
                subState = subState.copy(todos = action.todos)
        }
        if (subState !== newState.subState) {
            newState = newState.copy(subState = subState)
        }
        return newState
    }
}