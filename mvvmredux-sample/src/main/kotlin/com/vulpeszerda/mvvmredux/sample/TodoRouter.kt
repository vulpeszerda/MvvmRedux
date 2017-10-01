package com.vulpeszerda.mvvmredux.sample

import com.vulpeszerda.mvvmredux.RouterFactory
import com.vulpeszerda.mvvmredux.sample.create.TodoCreateActivity
import com.vulpeszerda.mvvmredux.sample.detail.TodoDetailActivity
import com.vulpeszerda.mvvmredux.sample.list.TodoListActivity

/**
 * Created by vulpes on 2017. 9. 5..
 */
class TodoRouter(delegate: RouterFactory.Delegate) :
        RouterFactory.Delegate by delegate {

    fun list(requestCode: Int? = null) {
        startActivity(TodoListActivity.createIntent(context), requestCode)
    }

    fun detail(uid: Long, requestCode: Int? = null) {
        startActivity(TodoDetailActivity.createIntent(context, uid), requestCode)
    }

    fun create(requestCode: Int? = null) {
        startActivity(TodoCreateActivity.createIntent(context), requestCode)
    }
}