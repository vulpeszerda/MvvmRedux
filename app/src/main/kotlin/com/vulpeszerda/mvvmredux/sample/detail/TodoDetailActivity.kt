package com.vulpeszerda.mvvmredux.sample.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.vulpeszerda.mvvmredux.library.BaseActivity
import com.vulpeszerda.mvvmredux.sample.R
import com.vulpeszerda.mvvmredux.sample.databinding.TodoDetailBinding

/**
 * Created by vulpes on 2017. 8. 31..
 */
class TodoDetailActivity : BaseActivity() {

    private lateinit var binding: TodoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.todo_detail)

        val uid = intent.getLongExtra(EXTRA_UID, -1)
    }

    companion object {

        private const val EXTRA_UID = "EXTRA_UID"

        fun createIntent(context: Context, uid: Long): Intent {
            return Intent(context, TodoDetailActivity::class.java)
                    .apply { putExtra(EXTRA_UID, uid) }
        }
    }

}
