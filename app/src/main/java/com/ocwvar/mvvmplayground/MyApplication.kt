package com.ocwvar.mvvmplayground

import android.app.Application
import com.ocwvar.mvvmplayground.network.NetworkRequest

/**
 * used in [AndroidManifest.xml] as application name
 */
@Suppress("unused")
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkRequest.init(this)
    }

}