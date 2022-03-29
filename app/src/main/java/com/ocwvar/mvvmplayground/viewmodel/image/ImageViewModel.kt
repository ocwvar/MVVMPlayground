package com.ocwvar.mvvmplayground.viewmodel.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ocwvar.mvvmplayground.base.BaseViewModel
import com.ocwvar.mvvmplayground.model.ImageModel
import com.ocwvar.mvvmplayground.network.Remote
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.ResponseBody

@Suppress("BlockingMethodInNonBlockingContext")
class ImageViewModel(
    remoteModels: Remote.Models,
    workerDispatcher: CoroutineDispatcher
) : BaseViewModel(workerDispatcher) {

    private val model: ImageModel = ImageModel(remoteModels)
    private val mLiveData: MutableLiveData<ImageModel.Model> = MutableLiveData()

    val liveData: LiveData<ImageModel.Model>
        get() = this.mLiveData

    /**
     * begin to fetch model
     */
    override fun fetch() {
        super.beginFetch {
            val responseBody: ResponseBody = this.model.getModel()
            if (responseBody.contentLength() <= 0) {
                postError("Content length is 0")
                return@beginFetch
            }

            val bytes: ByteArray = responseBody.bytes()
            this.mLiveData.postValue(ImageModel.Model(bytes))
        }
    }
}