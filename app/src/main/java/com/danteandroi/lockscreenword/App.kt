package com.danteandroi.lockscreenword

import android.app.Application
import com.blankj.utilcode.util.Utils
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @author Du Wenyu
 * 2022/2/13
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        Timber.plant(Timber.DebugTree())
    }

}