package com.vulpeszerda.mvvmredux.sample.list

import android.support.v7.app.AlertDialog
import com.vulpeszerda.mvvmredux.ContextService
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.BaseExtraHandler

class TodoListExtraHandler(
    contextService: ContextService
) : BaseExtraHandler("TodoListExtraHandler", contextService) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        when (extra) {
            is TodoListEvent.ShowClearedToast ->
                toast("Cleared")
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