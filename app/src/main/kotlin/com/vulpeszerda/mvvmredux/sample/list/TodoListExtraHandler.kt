package com.vulpeszerda.mvvmredux.sample.list

import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.vulpeszerda.mvvmredux.library.AbsExtraHandler
import com.vulpeszerda.mvvmredux.library.SideEffect

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoListExtraHandler(private val activity: TodoListActivity,
                           errorHandler: (Throwable) -> Unit) :
        AbsExtraHandler<TodoListUiEvent>(activity, errorHandler) {

    override fun onExtraSideEffect(extra: SideEffect.Extra) {
        when (extra) {
            is TodoListSideEffect.ShowClearedToast ->
                Toast.makeText(activity, "Cleared", Toast.LENGTH_SHORT).show()
            is TodoListSideEffect.ShowClearConfirm ->
                AlertDialog.Builder(activity).setTitle("Confirm")
                        .setMessage("Are you sure to clear all todo?")
                        .setPositiveButton("Clear all") { _, _ ->
                            emitUiEvent(TodoListUiEvent.ConfirmClearAll())
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
        }
    }
}