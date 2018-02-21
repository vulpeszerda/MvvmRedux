package com.vulpeszerda.mvvmredux.sample

import android.support.annotation.CallSuper
import com.vulpeszerda.mvvmredux.*
import com.vulpeszerda.mvvmredux.sample.create.TodoCreateActivity
import com.vulpeszerda.mvvmredux.sample.detail.TodoDetailActivity
import com.vulpeszerda.mvvmredux.sample.list.TodoListActivity

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class BaseNavigator(tag: String, contextWrapper: ContextWrapper) :
    AbsReduxNavigator(tag, contextWrapper) {

    constructor(tag: String, activity: ReduxActivity) : this(tag, ActivityContextWrapper(activity))
    constructor(tag: String, fragment: ReduxFragment) : this(tag, FragmentContextWrapper(fragment))

    @CallSuper
    override fun navigate(navigation: ReduxEvent.Navigation) {
        when (navigation) {
            is GlobalEvent.NavigateCreate ->
                startActivity(TodoCreateActivity.createIntent(context), navigation.requestCode)
            is GlobalEvent.NavigateDetail ->
                startActivity(
                    TodoDetailActivity.createIntent(context, navigation.uid),
                    navigation.requestCode
                )
            is GlobalEvent.NavigateList ->
                startActivity(TodoListActivity.createIntent(context), navigation.requestCode)
            is GlobalEvent.NavigateFinish ->
                finish()
        }
    }
}