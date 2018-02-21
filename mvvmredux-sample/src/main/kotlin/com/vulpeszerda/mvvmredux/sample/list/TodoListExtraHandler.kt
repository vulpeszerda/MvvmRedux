package com.vulpeszerda.mvvmredux.sample.list

import android.support.v7.app.AlertDialog
import com.vulpeszerda.mvvmredux.AbsReduxExtraHandler
import com.vulpeszerda.mvvmredux.ReduxEvent

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoListExtraHandler(activity: TodoListActivity) :
    AbsReduxExtraHandler("TodoListExtraHandler", activity) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        when (extra) {
            is TodoListEvent.ShowClearedToast ->
                toast("Cleared")
            is TodoListEvent.ShowClearConfirm ->
                AlertDialog.Builder(getActivityOrThrow()).setTitle("Confirm")
                    .setMessage("Are you sure to clear all todo?")
                    .setPositiveButton("Clear all") { _, _ ->
                        publishEvent(TodoListEvent.ConfirmClearAll())
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
        }
    }
}