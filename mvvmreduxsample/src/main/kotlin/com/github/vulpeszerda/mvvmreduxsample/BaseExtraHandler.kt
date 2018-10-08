package com.github.vulpeszerda.mvvmreduxsample

import com.github.vulpeszerda.mvvmreduxsample.create.TodoCreateActivity
import com.github.vulpeszerda.mvvmreduxsample.detail.TodoDetailActivity
import com.github.vulpeszerda.mvvmreduxsample.list.TodoListActivity

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
