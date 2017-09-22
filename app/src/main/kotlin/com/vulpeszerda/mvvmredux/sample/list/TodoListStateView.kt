package com.vulpeszerda.mvvmredux.sample.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.vulpeszerda.mvvmredux.library.BaseActivityStateView
import com.vulpeszerda.mvvmredux.library.GlobalState
import com.vulpeszerda.mvvmredux.sample.model.Todo
import io.reactivex.Observable
import kotlinx.android.synthetic.main.todo_list.*

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoListStateView(
        private val activity: TodoListActivity,
        errorHandler: (Throwable) -> Unit) :
        BaseActivityStateView<TodoListState, TodoListUiEvent>(activity, errorHandler) {

    private val adapter: TodoListAdapter by lazy {
        TodoListAdapter(object : TodoListAdapter.ActionHandler {

            override fun onClicked(todo: Todo) {
                emitUiEvent(TodoListUiEvent.ClickDetail(todo.uid))
            }

        })
    }

    override val events: Observable<TodoListUiEvent>
        get() = super.events
                .mergeWith(RxView.clicks(btn_new).map { TodoListUiEvent.ClickCreate() })
                .mergeWith(RxView.clicks(btn_clear).map { TodoListUiEvent.ClickClearAll() })

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity)
    }

    override fun onStateChanged(prev: GlobalState<TodoListState>?,
                                curr: GlobalState<TodoListState>?) {

        if (prev?.subState?.todos !== curr?.subState?.todos) {
            val currTodos = curr?.subState?.todos ?: ArrayList()
            val diff = DiffUtil.calculateDiff(TodoDiffCallback(adapter.todos, currTodos))
            adapter.todos.apply {
                clear()
                addAll(currTodos)
            }
            diff.dispatchUpdatesTo(adapter)
        }

        if (prev?.subState?.loading != curr?.subState?.loading) {
            if (curr?.subState?.loading == true) {
                showProgressDialog("Loading..")
            } else {
                hideProgressDialog()
            }
        }
    }

    private class TodoDiffCallback(private val prev: List<Todo>,
                                   private val curr: List<Todo>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                prev[oldItemPosition].uid == curr[newItemPosition].uid

        override fun getOldListSize(): Int = prev.size

        override fun getNewListSize(): Int = curr.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                prev[oldItemPosition] == curr[newItemPosition]

    }
}