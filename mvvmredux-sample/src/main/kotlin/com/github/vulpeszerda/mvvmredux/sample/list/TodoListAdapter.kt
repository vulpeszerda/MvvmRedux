package com.github.vulpeszerda.mvvmredux.sample.list

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.github.vulpeszerda.mvvmredux.sample.model.Todo

class TodoListAdapter(private val actionHandler: ActionHandler) :
    RecyclerView.Adapter<TodoViewHolder>() {

    interface ActionHandler : TodoViewHolder.ActionHandler

    val todos = ArrayList<Todo>()

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder.create(parent, actionHandler)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }
}