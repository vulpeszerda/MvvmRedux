package com.vulpeszerda.mvvmredux.sample.list

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.vulpeszerda.mvvmredux.library.BaseActivity
import com.vulpeszerda.mvvmredux.library.GlobalState
import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.R
import com.vulpeszerda.mvvmredux.sample.create.TodoCreateActivity
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import com.vulpeszerda.mvvmredux.sample.databinding.TodoListBinding
import com.vulpeszerda.mvvmredux.sample.detail.TodoDetailActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class TodoListActivity : BaseActivity(), TodoListViewModelDelegate {

    private val eventSubject = PublishSubject.create<TodoListUiEvent>()

    private lateinit var binding: TodoListBinding
    private lateinit var viewModel: TodoListViewModel

    override val events: Flowable<TodoListUiEvent> by lazy {
        Observable.merge(
                RxView.clicks(binding.btnNew).map { TodoListUiEvent.ClickCreate() },
                RxView.clicks(binding.btnClear).map { TodoListUiEvent.ClickClearAll() },
                eventSubject)
                .toFlowable(BackpressureStrategy.DROP)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.todo_list)

        viewModel = ViewModelProviders.of(this).get(TodoListViewModel::class.java)
        viewModel.initialize(this,
                GlobalState(restoreStateFromBundle(savedInstanceState)),
                TodoDatabase.getInstance(this))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TodoListViewModel::class.java)
        viewModel.initialize(this,
                GlobalState(restoreStateFromBundle(savedInstanceState)),
                TodoDatabase.getInstance(this))
    }

    override fun onStart() {
        super.onStart()
        eventSubject.onNext(TodoListUiEvent.Refresh())
    }

    private fun restoreStateFromBundle(bundle: Bundle?): TodoListState {
        return TodoListState()
    }

    override fun navigate(navigation: SideEffect.Navigation) {
        when (navigation) {
            is TodoListSideEffect.NavigateDetail ->
                startActivity(TodoDetailActivity.createIntent(this, navigation.uid))
            is TodoListSideEffect.NavigateCreate ->
                startActivity(TodoCreateActivity.createIntent(this))
        }
    }

    override fun handleExtraSideEffect(sideEffect: SideEffect.Extra) {
        when (sideEffect) {
            is TodoListSideEffect.ShowClearedToast ->
                Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show()
            is TodoListSideEffect.ShowClearConfirm ->
                AlertDialog.Builder(this).setTitle("Confirm")
                        .setMessage("Are you sure to clear all todo?")
                        .setPositiveButton("Clear all") { _, _ ->
                            eventSubject.onNext(TodoListUiEvent.ConfirmClearAll())
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
        }
    }

    override fun onError(throwable: Throwable, tag: String?, vararg otherArguments: Any) {
    }
}
