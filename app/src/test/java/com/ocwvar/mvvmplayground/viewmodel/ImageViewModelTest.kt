package com.ocwvar.mvvmplayground.viewmodel

import androidx.lifecycle.Observer
import com.ocwvar.mvvmplayground.base.BaseUnitTest
import com.ocwvar.mvvmplayground.general.Constant
import com.ocwvar.mvvmplayground.model.ImageModel
import com.ocwvar.mvvmplayground.network.Remote
import com.ocwvar.mvvmplayground.viewmodel.image.ImageViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
class ImageViewModelTest: BaseUnitTest() {

    @Mock
    private lateinit var mockRemoteModels: Remote.Models

    @Mock
    private lateinit var mockDataObserver: Observer<ImageModel.Model>

    @Mock
    private lateinit var mockLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var mockErrorObserver: Observer<String>

    @Mock
    private lateinit var mockResponseBody: ResponseBody

    @Test
    fun test_fetch_with_success() = testingScope {
        val bytes = ByteArray(10)
        Mockito.`when`(mockResponseBody.bytes()).thenReturn(bytes)
        Mockito.`when`(mockResponseBody.contentLength()).thenReturn(bytes.size.toLong())
        Mockito.`when`(mockRemoteModels.getImageBytes(Constant.Url.IMAGE_URL)).thenReturn(mockResponseBody)

        ImageViewModel(mockRemoteModels, getTestDispatcher()).apply {
            this.liveData.observeForever(mockDataObserver)
            this.errorStatusLiveData.observeForever(mockErrorObserver)
            this.loadingStatusLiveData.observeForever(mockLoadingObserver)
            this.fetch()
        }

        Mockito.verify(mockLoadingObserver).onChanged(true)
        Mockito.verify(mockDataObserver).onChanged(ImageModel.Model(bytes))
        Mockito.verify(mockLoadingObserver).onChanged(false)
        Mockito.verify(mockErrorObserver, Mockito.never()).onChanged(Mockito.anyString())
    }

    @Test
    fun test_fetch_with_request_exception() = testingScope {
        Mockito.`when`(mockRemoteModels.getImageBytes(Constant.Url.IMAGE_URL))
            .thenThrow(RuntimeException())

        ImageViewModel(mockRemoteModels, getTestDispatcher()).apply {
            this.liveData.observeForever(mockDataObserver)
            this.errorStatusLiveData.observeForever(mockErrorObserver)
            this.loadingStatusLiveData.observeForever(mockLoadingObserver)
            this.fetch()
        }

        Mockito.verify(mockLoadingObserver).onChanged(true)
        Mockito.verify(mockLoadingObserver).onChanged(false)
        Mockito.verify(mockErrorObserver).onChanged(Mockito.anyString())
        Mockito.verify(mockDataObserver, Mockito.never()).onChanged(Mockito.any())
    }

}