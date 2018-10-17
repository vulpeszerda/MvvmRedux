package com.github.vulpeszerda.mvvmreduxsample.list

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseStateView
import com.github.vulpeszerda.mvvmreduxsample.model.Todo
import kotlinx.android.synthetic.main.todo_list.*

class TodoListStateView(
    contextDelegate: ContextDelegate,
    private val extraHandler: TodoListExtraHandler,
    private val viewModel: TodoListViewModel
) : BaseStateView("TodoListStateView", contextDelegate) {

    private val adapter: TodoListAdapter by lazy {
        TodoListAdapter(object :
            TodoListAdapter.ActionHandler {

            override fun onClicked(todo: Todo) {
                extraHandler.navigateDetail(todo.uid)
            }

        })
    }

    init {
        viewModel.subscribe({ it.subState.todos }) { _, curr ->
            val currTodos = curr.subState.todos
            val diff = DiffUtil.calculateDiff(TodoDiffCallback(adapter.todos, currTodos))
            adapter.todos.apply {
                clear()
                addAll(currTodos)
            }
            diff.dispatchUpdatesTo(adapter)
            Log.d("TEST111", "items : ${currTodos.size}")
        }
        viewModel.subscribe({ it.subState.loading }) { _, curr ->
            if (curr.subState.loading) {
                showProgressDialog("Loading..")
            } else {
                hideProgressDialog()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(activity)

        btn_new.setOnClickListener {
            extraHandler.navigateCreate()
        }

        btn_clear.setOnClickListener {
            extraHandler.showClearConfirm()
        }
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