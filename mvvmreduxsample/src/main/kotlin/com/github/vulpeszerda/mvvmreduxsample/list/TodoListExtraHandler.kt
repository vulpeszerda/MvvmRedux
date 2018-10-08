package com.github.vulpeszerda.mvvmreduxsample.list

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.vulpeszerda.mvvmredux.ReduxEvent
import com.github.vulpeszerda.mvvmreduxsample.BaseExtraHandler

class TodoListExtraHandler(
    contextDelegate: com.github.vulpeszerda.mvvmredux.ContextDelegate
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