package com.vulpeszerda.mvvmredux

import android.support.v7.app.AppCompatActivity

open class ReduxActivity :
    AppCompatActivity(),
    ReduxEventPublisher by ReduxEventPublisher.Impl()
