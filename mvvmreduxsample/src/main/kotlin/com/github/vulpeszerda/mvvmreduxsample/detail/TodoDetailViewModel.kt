package com.github.vulpeszerda.mvvmreduxsample.detail

import com.github.vulpeszerda.mvvmredux.AbsReduxViewModel
import com.github.vulpeszerda.mvvmredux.ReduxEvent
import com.github.vulpeszerda.mvvmreduxsample.GlobalState
import com.github.vulpeszerda.mvvmreduxsample.database.TodoDatabase
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class TodoDetailViewModel(private val database: TodoDatabase) :
    AbsReduxViewModel<GlobalState<TodoDetailState>>() {

    private val blockingActionSubject = PublishSubject.create<ReduxEvent>()

    override fun eventTransformer(
        events: Observable<ReduxEvent>,
        getState: () -> GlobalState<TodoDetailState>
    ): Observable<ReduxEvent> {
        return blockingActionSubject
            .toFlowable(BackpressureStrategy.DROP)
            .flatMap({
                handleEvent(it).toFlowable(BackpressureStrategy.ERROR)
            }, 1)
            .toObservable()
            .mergeWith(super.eventTransformer(events, getState)
                .flatMap {
                    when (it) {
                        is TodoDetailEvent.CheckTodo,
                        is TodoDetailEvent.Refresh -> {
                            blockingActionSubject.onNext(it)
                            Observable.empty()
                        }
                        else -> handleEvent(it)
                    }
                })
    }

    private fun handleEvent(event: ReduxEvent): Observable<ReduxEvent> =
        when (event) {
            is TodoDetailEvent.Refresh -> refresh(event.todoUid, event.silent)
            is TodoDetailEvent.CheckTodo -> check(event.todoUid, event.checked)
            else -> Observable.just(event)
        }

    private fun refresh(todoUid: Long, silent: Boolean): Observable<ReduxEvent> {
        return Maybe.fromCallable { database.todoDao().findByUid(todoUid) }
            .subscribeOn(Schedulers.io())
            .toSingle()
            .flatMapObservable<ReduxEvent> { todo ->
                Observable.fromArray(
                    TodoDetailEvent.SetTodo(todo),
                    TodoDetailEvent.SetError(null)
                )
            }
            .onErrorReturn {
                TodoDetailEvent.SetError(
                    it
                )
            }
            .let {
                if (silent) it else it
                    .startWith(
                        TodoDetailEvent.SetLoading(
                            true
                        )
                    )
                    .concatWith(
                        Observable.just(
                            TodoDetailEvent.SetLoading(
                                false
                            )
                        )
                    )
            }
    }

    private fun check(todoUid: Long, checked: Boolean): Observable<ReduxEvent> {
        return Maybe.fromCallable { database.todoDao().findByUid(todoUid) }
            .flatMapSingleElement { todo ->
                val changed = todo.apply { isCompleted = checked }
                Completable.fromAction { database.todoDao().update(changed) }
                    .toSingleDefault(changed)
            }
            .subscribeOn(Schedulers.io())
            .toSingle()
            .flatMapObservable<ReduxEvent> { todo ->
                Observable.fromArray(
                    TodoDetailEvent.SetTodo(todo),
                    TodoDetailEvent.SetError(null),
                    TodoDetailEvent.ShowCheckedToast(
                        checked
                    )
                )
            }
            .onErrorReturn {
                TodoDetailEvent.SetError(
                    it
                )
            }
            .startWith(TodoDetailEvent.SetLoading(true))
            .concatWith(
                Observable.just(
                    TodoDetailEvent.SetLoading(
                        false
                    )
                )
            )
    }

    override fun reduceState(
        state: GlobalState<TodoDetailState>,
        event: ReduxEvent.State
    ): GlobalState<TodoDetailState> {
        var newState = super.reduceState(state, event)
        var subState = newState.subState
        when (event) {
            is TodoDetailEvent.SetLoading ->
                subState = subState.copy(loading = event.loading)
            is TodoDetailEvent.SetError ->
                subState = subState.copy(error = event.error)
            is TodoDetailEvent.SetTodo -> {
                subState = subState.copy(todo = event.todo)
            }
        }
        if (subState !== newState.subState) {
            newState = newState.copy(subState = subState)
        }
        return newState
    }

}