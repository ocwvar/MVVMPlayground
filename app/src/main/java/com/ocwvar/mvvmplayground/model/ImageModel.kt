package com.ocwvar.mvvmplayground.model

import com.ocwvar.mvvmplayground.general.Constant
import com.ocwvar.mvvmplayground.network.Remote
import okhttp3.ResponseBody

class ImageModel(private val remoteModels: Remote.Models) {

    data class Model(val bytes: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Model

            if (!bytes.contentEquals(other.bytes)) return false

            return true
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    suspend fun getModel(): ResponseBody {
        return this.remoteModels.getImageBytes(Constant.Url.IMAGE_URL)
    }

}