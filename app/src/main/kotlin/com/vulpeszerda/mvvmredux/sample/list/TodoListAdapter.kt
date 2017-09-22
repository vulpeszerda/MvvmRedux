package com.vulpeszerda.mvvmredux.sample.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.vulpeszerda.mvvmredux.sample.model.Todo

/**
 * Created by vulpes on 2017. 9. 21..
 */
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