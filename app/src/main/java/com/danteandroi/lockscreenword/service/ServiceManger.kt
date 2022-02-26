package com.danteandroi.lockscreenword.service

import android.content.Intent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.DelicateCoroutinesApi

/**
 * @author Du Wenyu
 * 2022/2/19
 */
object ServiceManger {

    private val serviceIntent = Intent(Utils.getApp().applicationContext, WordService::class.java)

    fun start(data: String) {
        ContextCompat.startForegroundService(
            Utils.getApp().applicationContext,
            serviceIntent.apply {
                putExtra(WORDS_KEY, data)
            })
    }

    fun stop() {
        Utils.getApp().stopService(serviceIntent)
    }

}