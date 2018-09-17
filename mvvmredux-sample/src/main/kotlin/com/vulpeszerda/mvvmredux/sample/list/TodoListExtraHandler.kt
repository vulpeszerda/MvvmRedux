package com.vulpeszerda.mvvmredux.sample.list

import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.vulpeszerda.mvvmredux.ContextDelegate
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.BaseExtraHandler

class TodoListExtraHandler(
    contextDelegate: ContextDelegate
) : BaseExtraHandler("TodoListExtraHandler", contextDelegate) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        when (extra) {
            is TodoListEvent.ShowClearedToast ->
                Toast.makeText(getContextOrThrow(), "Cleared", Toast.LENGTH_SHORT).show()
            is TodoListEvent.ShowClearConfirm ->
                AlertDialog.Builder(getActivityOrThrow()).setTitle("Confirm")
                    .setMessage("Are you sure to clear all todo?")
                    .setPositiveButton("Clear all") { _, _ ->
                        publishEvent(TodoListEvent.ConfirmClearAll)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            else ->
                super.onExtraEvent(extra)
        }
    }
}