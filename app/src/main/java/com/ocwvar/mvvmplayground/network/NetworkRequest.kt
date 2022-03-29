package com.ocwvar.mvvmplayground.network

import android.app.Application
import com.ocwvar.mvvmplayground.general.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkRequest {

    private lateinit var mRetrofit: Retrofit
    private lateinit var mRemote: Remote

    /**
     * @return remote models requester
     */
    val remoteModels: Remote.Models
        get() = this.mRemote.get

    /**
     * init retrofit with application
     */
    fun init(application: Application) {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(RequestInterceptor(application))
            .callTimeout(2, TimeUnit.SECONDS)
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()

        this.mRetrofit = Retrofit
            .Builder()
            .baseUrl(Constant.Url.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.mRemote = Remote(this.mRetrofit)
    }

}