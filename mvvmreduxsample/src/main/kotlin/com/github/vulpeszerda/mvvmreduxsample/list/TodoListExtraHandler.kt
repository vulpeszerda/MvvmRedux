package com.github.vulpeszerda.mvvmreduxsample.list

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.BaseExtraHandler

class TodoListExtraHandler(
    contextDelegate: ContextDelegate,
    private val viewModel: TodoListViewModel
) : BaseExtraHandler("TodoListExtraHandler", contextDelegate) {

    fun showClearedToast() {
        ensureResumed {
            Toast.makeText(getContextOrThrow(), "Cleared", Toast.LENGTH_SHORT).show()
        }
    }

    fun showClearConfirm() {
        ensureResumed {
            AlertDialog.Builder(getActivityOrThrow()).setTitle("Confirm")
                .setMessage("Are you sure to clear all todo?")
                .setPositiveButton("Clear all") { _, _ ->
                    viewModel.clearAll()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}