package com.github.vulpeszerda.mvvmreduxsample

import com.github.vulpeszerda.mvvmredux.AbsReduxExtraHandler
import com.github.vulpeszerda.mvvmredux.ContextDelegate
import com.github.vulpeszerda.mvvmreduxsample.create.TodoCreateActivity
import com.github.vulpeszerda.mvvmreduxsample.detail.TodoDetailActivity
import com.github.vulpeszerda.mvvmreduxsample.list.TodoListActivity

open class BaseExtraHandler(
    tag: String = "BaseExtraHandler",
    contextDelegate: ContextDelegate
) : AbsReduxExtraHandler(tag, contextDelegate) {

    fun navigateCreate(requestCode: Int? = null) {
        ensureResumed {
            startActivity(
                TodoCreateActivity.createIntent(getContextOrThrow()),
                requestCode
            )
        }
    }

    fun navigateDetail(uid: Long, requestCode: Int? = null) {
        ensureResumed {
            startActivity(
                TodoDetailActivity.createIntent(getContextOrThrow(), uid),
                requestCode
            )
        }
    }

    fun navigateList(requestCode: Int? = null) {
        ensureResumed {
            startActivity(
                TodoListActivity.createIntent(getContextOrThrow()),
                requestCode
            )
        }
    }

    fun navigateFinish() {
        ensureResumed {
            finish()
        }
    }

    fun error(throwable: Throwable) {
        throwable.printStackTrace()
    }
}
