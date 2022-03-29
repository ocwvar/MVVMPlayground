package com.ocwvar.mvvmplayground.viewmodel

import androidx.lifecycle.Observer
import com.ocwvar.mvvmplayground.base.BaseUnitTest
import com.ocwvar.mvvmplayground.base.GeneralModel
import com.ocwvar.mvvmplayground.general.Constant
import com.ocwvar.mvvmplayground.model.HomePageModel
import com.ocwvar.mvvmplayground.network.Remote
import com.ocwvar.mvvmplayground.viewmodel.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private val mockContent: String = "hello"

    @Mock
    private lateinit var mockRemoteModels: Remote.Models

    @Mock
    private lateinit var mockDataObserver: Observer<HomePageModel.Model>

    @Mock
    private lateinit var mockLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var mockErrorObserver: Observer<String>

    @Test
    fun test_fetch_with_success() = testingScope {
        Mockito.`when`(mockRemoteModels.getHomeModel(Constant.Proxy.HOME_MODEL))
            .thenReturn(GeneralModel(
                code = Constant.ErrorCode.NO_ERROR,
                data = HomePageModel.Model(
                    content = mockContent
                )
            ))

        HomeViewModel(mockRemoteModels, getTestDispatcher()).apply {
            this.liveData.observeForever(mockDataObserver)
            this.errorStatusLiveData.observeForever(mockErrorObserver)
            this.loadingStatusLiveData.observeForever(mockLoadingObserver)
            this.fetch()
        }

        Mockito.verify(mockLoadingObserver).onChanged(true)
        Mockito.verify(mockErrorObserver, Mockito.never()).onChanged(Mockito.anyString())
        Mockito.verify(mockDataObserver).onChanged(HomePageModel.Model(mockContent))
        Mockito.verify(mockLoadingObserver).onChanged(false)
    }

    @Test
    fun test_fetch_with_timeout() = testingScope {
        Mockito.`when`(mockRemoteModels.getHomeModel(Constant.Proxy.HOME_MODEL))
            .thenReturn(GeneralModel(
                code = Constant.ErrorCode.TIMEOUT,
                data = null
            ))

        HomeViewModel(mockRemoteModels, getTestDispatcher()).apply {
            this.liveData.observeForever(mockDataObserver)
            this.errorStatusLiveData.observeForever(mockErrorObserver)
            this.loadingStatusLiveData.observeForever(mockLoadingObserver)
            this.fetch()
        }

        Mockito.verify(mockLoadingObserver).onChanged(true)
        Mockito.verify(mockErrorObserver).onChanged(Constant.ErrorCode.TIMEOUT)
        Mockito.verify(mockDataObserver, Mockito.never()).onChanged(Mockito.any())
        Mockito.verify(mockLoadingObserver).onChanged(false)
    }

    @Test
    fun test_fetch_with_request_exception() = testingScope {
        Mockito.`when`(mockRemoteModels.getHomeModel(Constant.Proxy.HOME_MODEL))
            .thenThrow(RuntimeException())

        HomeViewModel(mockRemoteModels, getTestDispatcher()).apply {
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