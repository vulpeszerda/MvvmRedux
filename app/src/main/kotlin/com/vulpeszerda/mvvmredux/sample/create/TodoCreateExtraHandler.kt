package com.vulpeszerda.mvvmredux.sample.create

import android.widget.Toast
import com.vulpeszerda.mvvmredux.library.AbsExtraHandler
import com.vulpeszerda.mvvmredux.library.SideEffect

/**
 * Created by vulpes on 2017. 9. 21..
 */
class TodoCreateExtraHandler(
        private val activity: TodoCreateActivity,
        errorHandler: (Throwable) -> Unit) :
        AbsExtraHandler<TodoCreateUiEvent>(activity, errorHandler) {

    override fun onExtraSideEffect(extra: SideEffect.Extra) {
        if (extra is TodoCreateSideEffect.ShowFinishToast) {
            Toast.makeText(activity, "Todo created", Toast.LENGTH_SHORT).show()
        }
    }

}