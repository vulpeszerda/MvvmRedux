package com.vulpeszerda.mvvmredux.sample

import android.support.annotation.CallSuper
import com.vulpeszerda.mvvmredux.*
import com.vulpeszerda.mvvmredux.sample.create.TodoCreateActivity
import com.vulpeszerda.mvvmredux.sample.detail.TodoDetailActivity
import com.vulpeszerda.mvvmredux.sample.list.TodoListActivity

/**
 * Created by vulpes on 2017. 11. 23..
 */
open class BaseNavigator(tag: String, contextService: ContextService) :
    AbsReduxNavigator(tag, contextService) {

    constructor(tag: String, activity: ReduxActivity) : this(tag, ActivityContextService(activity))
    constructor(tag: String, fragment: ReduxFragment) : this(tag, FragmentContextService(fragment))

    @CallSuper
    override fun navigate(navigation: ReduxEvent.Navigation) {
        when (navigation) {
            is GlobalEvent.NavigateCreate ->
                startActivity(
                    TodoCreateActivity.createIntent(getContextOrThrow()),
                    navigation.requestCode
                )
            is GlobalEvent.NavigateDetail ->
                startActivity(
                    TodoDetailActivity.createIntent(getContextOrThrow(), navigation.uid),
                    navigation.requestCode
                )
            is GlobalEvent.NavigateList ->
                startActivity(
                    TodoListActivity.createIntent(getContextOrThrow()),
                    navigation.requestCode
                )
            is GlobalEvent.NavigateFinish ->
                finish()
        }
    }
}