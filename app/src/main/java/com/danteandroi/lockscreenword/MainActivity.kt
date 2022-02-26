package com.danteandroi.lockscreenword

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.danteandroi.lockscreenword.databinding.ActivityMainBinding
import com.danteandroi.lockscreenword.service.ServiceManger
import com.danteandroi.lockscreenword.source.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

private const val ENABLE_KEY = "enable"
@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val mainBinding by viewBinding<ActivityMainBinding>()
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding.startService.isChecked = SPUtils.getInstance().getBoolean(ENABLE_KEY)
        mainBinding.startService.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                SPUtils.getInstance().put(ENABLE_KEY, true)
                ServiceManger.start("")
                ToastUtils.showShort(R.string.start_service_hint)
            } else {
                SPUtils.getInstance().put(ENABLE_KEY, false)
                ServiceManger.stop()
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.allWords().collect {
                Timber.d("onCreate: ${it.size}")
            }
        }
    }


    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}