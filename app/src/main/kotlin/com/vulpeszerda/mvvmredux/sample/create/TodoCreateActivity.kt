package com.vulpeszerda.mvvmredux.sample.create

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.vulpeszerda.mvvmredux.library.BaseActivity
import com.vulpeszerda.mvvmredux.library.GlobalState
import com.vulpeszerda.mvvmredux.library.SideEffect
import com.vulpeszerda.mvvmredux.sample.R
import com.vulpeszerda.mvvmredux.sample.database.TodoDatabase
import com.vulpeszerda.mvvmredux.sample.databinding.TodoCreateBinding
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoCreateActivity : BaseActivity(), TodoCreateViewModelDelegate {

    private lateinit var binding: TodoCreateBinding
    private lateinit var viewModel: TodoCreateViewModel

    override val events: Flowable<TodoCreateUiEvent> by lazy {
        RxView.clicks(binding.btnSave)
                .map { Pair(binding.title.text?.toString(), binding.message.text?.toString()) }
                .filter { it.first != null && it.second != null }
                .map<TodoCreateUiEvent> { (title, message) ->
                    TodoCreateUiEvent.Save(title!!, message!!)
                }
                .toFlowable(BackpressureStrategy.DROP)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.todo_create)
        viewModel = ViewModelProviders.of(this).get(TodoCreateViewModel::class.java)
        viewModel.initialize(this, GlobalState(TodoCreateState()), TodoDatabase.getInstance(this))
    }

    override fun onError(throwable: Throwable, tag: String?, vararg otherArguments: Any) {
    }

    override fun navigate(navigation: SideEffect.Navigation) {
        if (navigation is TodoCreateSideEffect.NavigateFinish) {
            finish()
        }
    }

    override fun handleExtraSideEffect(sideEffect: SideEffect.Extra) {
        if (sideEffect is TodoCreateSideEffect.ShowFinishToast) {
            Toast.makeText(this, "Todo created", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TodoCreateActivity::class.java)
        }
    }
}