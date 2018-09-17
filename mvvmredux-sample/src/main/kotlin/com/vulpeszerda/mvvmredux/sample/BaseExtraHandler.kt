package com.vulpeszerda.mvvmredux.sample

import com.vulpeszerda.mvvmredux.AbsReduxExtraHandler
import com.vulpeszerda.mvvmredux.ContextDelegate
import com.vulpeszerda.mvvmredux.ReduxEvent
import com.vulpeszerda.mvvmredux.sample.create.TodoCreateActivity
import com.vulpeszerda.mvvmredux.sample.detail.TodoDetailActivity
import com.vulpeszerda.mvvmredux.sample.list.TodoListActivity

open class BaseExtraHandler(
    tag: String = "BaseExtraHandler",
    contextDelegate: ContextDelegate
) : AbsReduxExtraHandler(tag, contextDelegate) {

    override fun onExtraEvent(extra: ReduxEvent.Extra) {
        when (extra) {
            is GlobalEvent.NavigateCreate ->
                startActivity(
                    TodoCreateActivity.createIntent(getContextOrThrow()),
                    extra.requestCode
                )
            is GlobalEvent.NavigateDetail ->
                startActivity(
                    TodoDetailActivity.createIntent(getContextOrThrow(), extra.uid),
                    extra.requestCode
                )
            is GlobalEvent.NavigateList ->
                startActivity(
                    TodoListActivity.createIntent(getContextOrThrow()),
                    extra.requestCode
                )
            is GlobalEvent.NavigateFinish ->
                finish()
            is GlobalEvent.Error ->
                extra.throwable.printStackTrace()
        }
    }
}
