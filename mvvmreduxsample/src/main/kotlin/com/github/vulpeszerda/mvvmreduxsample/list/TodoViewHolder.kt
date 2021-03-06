package com.github.vulpeszerda.mvvmreduxsample.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.vulpeszerda.mvvmreduxsample.R
import com.github.vulpeszerda.mvvmreduxsample.model.Todo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.todo_item.*

class TodoViewHolder(override val containerView: View, actionHandler: ActionHandler) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    interface ActionHandler {
        fun onClicked(todo: Todo)
    }

    private var boundTodo: Todo? = null

    init {
        containerView.setOnClickListener {
            boundTodo?.let { actionHandler.onClicked(it) }
        }
    }

    fun bind(todo: Todo) {
        boundTodo = todo
        title.text = todo.title
        message.text = todo.message
    }

    companion object {

        fun create(parentView: ViewGroup, actionHandler: ActionHandler): TodoViewHolder {
            return TodoViewHolder(
                LayoutInflater.from(parentView.context)
                    .inflate(R.layout.todo_item, parentView, false),
                actionHandler
            )
        }
    }
}