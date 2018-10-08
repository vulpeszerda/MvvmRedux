package com.github.vulpeszerda.mvvmreduxsample.list

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vulpeszerda.mvvmreduxsample.*
import com.github.vulpeszerda.mvvmreduxsample.model.Todo
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.todo_list.*

class TodoListStateView(
    contextDelegate: ContextDelegate
) : BaseStateView<TodoListState>("TodoListStateView", contextDelegate) {

    private val adapter: TodoListAdapter by lazy {
        TodoListAdapter(object :
            TodoListAdapter.ActionHandler {

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
                    val diff = DiffUtil.calculateDiff(
                        TodoDiffCallback(
                            adapter.todos,
                            currTodos
                        )
                    )
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