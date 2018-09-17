package com.vulpeszerda.mvvmredux.sample.list

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.vulpeszerda.mvvmredux.ContextDelegate
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.StateConsumer
import com.vulpeszerda.mvvmredux.sample.BaseStateView
import com.vulpeszerda.mvvmredux.sample.GlobalEvent
import com.vulpeszerda.mvvmredux.sample.model.Todo
import io.reactivex.Observable
import kotlinx.android.synthetic.main.todo_list.*

class TodoListStateView(
    contextDelegate: ContextDelegate
) : BaseStateView<TodoListState>("TodoListStateView", contextDelegate) {

    private val adapter: TodoListAdapter by lazy {
        TodoListAdapter(object : TodoListAdapter.ActionHandler {

            override fun onClicked(todo: Todo) {
                publishEvent(GlobalEvent.NavigateDetail(todo.uid))
            }

        })
    }

    override val events: Observable<ReduxEvent>
        get() = super.events
            .mergeWith(RxView.clicks(btn_new).map { GlobalEvent.NavigateCreate() })
            .mergeWith(RxView.clicks(btn_clear).map { TodoListEvent.ShowClearConfirm() })

    init {
        addConsumer(
            StateConsumer.createFromAction(
                hasChange = { prev, curr -> prev.subState.todos !== curr.subState.todos },
                apply = { _, curr ->
                    val currTodos = curr.subState.todos
                    val diff = DiffUtil.calculateDiff(TodoDiffCallback(adapter.todos, currTodos))
                    adapter.todos.apply {
                        clear()
                        addAll(currTodos)
                    }
                    diff.dispatchUpdatesTo(adapter)
                })
        )
        addConsumer(
            StateConsumer.createFromAction(
                hasChange = { prev, curr -> prev.subState.loading != curr.subState.loading },
                apply = { _, curr ->
                    if (curr.subState.loading) {
                        showProgressDialog("Loading..")
                    } else {
                        hideProgressDialog()
                    }
                })
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity)
    }

    private class TodoDiffCallback(
        private val prev: List<Todo>,
        private val curr: List<Todo>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            prev[oldItemPosition].uid == curr[newItemPosition].uid

        override fun getOldListSize(): Int = prev.size

        override fun getNewListSize(): Int = curr.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            prev[oldItemPosition] == curr[newItemPosition]

    }
}