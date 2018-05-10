package com.vulpeszerda.mvvmredux

import android.support.v4.app.Fragment

open class ReduxFragment :
    Fragment(),
    ReduxEventPublisher by ReduxEventPublisher.Impl()
