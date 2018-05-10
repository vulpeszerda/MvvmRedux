package com.vulpeszerda.mvvmredux

interface Component<T> {

    val viewModel: ReduxViewModel<T>

    val navigator: ReduxNavigator

    val extraHandler: ReduxExtraHandler

    val errorHandler: ReduxErrorHandler

    val stateView: ReduxStateView<T>
}