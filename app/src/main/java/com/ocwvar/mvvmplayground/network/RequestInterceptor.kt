package com.ocwvar.mvvmplayground.network

import android.content.Context
import android.content.res.AssetManager
import com.ocwvar.mvvmplayground.general.Constant
import okhttp3.*
import java.net.HttpURLConnection

class RequestInterceptor(
    private val applicationContext: Context
) : Interceptor {

    private val assetManager: AssetManager = this.applicationContext.assets

    override fun intercept(chain: Interceptor.Chain): Response {
        Thread.sleep(1000)

        return when(chain.request().url().pathSegments().last()) {
            Constant.Proxy.HOME_MODEL -> {
                assembleAsResponse(
                    fromRequest = chain.request(),
                    responseString = getRawStringFromAsset("response_home_model.json")
                )
            }

            Constant.Proxy.ACCOUNT_INFO_MODEL -> {
                assembleAsResponse(
                    fromRequest = chain.request(),
                    responseString = getRawStringFromAsset("response_account_info_model.json")
                )
            }

            Constant.Proxy.ACCOUNT_STATUS_MODEL -> {
                assembleAsResponse(
                    fromRequest = chain.request(),
                    responseString = getRawStringFromAsset("response_account_status_model.json")
                )
            }

            else -> {
                chain.proceed(chain.request())
            }
        }
    }

    private fun assembleAsResponse(
        fromRequest: Request,
        responseString: String,
        responseCode: Int = HttpURLConnection.HTTP_OK
    ): Response {
        return Response
            .Builder()
            .message(responseCode.toString())
            .protocol(Protocol.HTTP_1_0)
            .code(responseCode)
            .request(fromRequest)
            .body(
                ResponseBody.create(MediaType.parse("application/json"), responseString)
            )
            .build()
    }

    private fun getRawStringFromAsset(fileName: String): String {
        with(this.assetManager.open(fileName)) {
            return String(this.readBytes())
        }
    }

}