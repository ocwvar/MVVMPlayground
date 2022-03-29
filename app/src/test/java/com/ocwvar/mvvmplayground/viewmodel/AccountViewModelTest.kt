package com.ocwvar.mvvmplayground.viewmodel

import androidx.lifecycle.Observer
import com.ocwvar.mvvmplayground.base.BaseUnitTest
import com.ocwvar.mvvmplayground.base.GeneralModel
import com.ocwvar.mvvmplayground.general.Constant
import com.ocwvar.mvvmplayground.model.AccountModel
import com.ocwvar.mvvmplayground.network.Remote
import com.ocwvar.mvvmplayground.viewmodel.account.AccountViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AccountViewModelTest : BaseUnitTest() {

    private val mockUserName: String = "Jimmy"
    private val mockUserTitle: String = "Android Developer"
    private val mockUserStatus: String = "ONLINE"

    @Mock
    private lateinit var mockRemoteModels: Remote.Models

    @Mock
    private lateinit var mockDataObserver: Observer<AccountModel.DisplayModel>

    @Mock
    private lateinit var mockLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var mockErrorObserver: Observer<String>

    @Test
    fun test_fetch_with_success() = testingScope {
        Mockito.`when`(mockRemoteModels.getUserInfoModel(Constant.Proxy.ACCOUNT_INFO_MODEL))
            .thenReturn(
                GeneralModel(
                    code = Constant.ErrorCode.NO_ERROR,
                    data = AccountModel.UserInfoModel(
                        name = mockUserName,
                        title = mockUserTitle
                    )
                )
            )

        Mockito.`when`(
            mockRemoteModels.getStatusModel(
                mockUserName,
                Constant.Proxy.ACCOUNT_STATUS_MODEL
            )
        )
            .thenReturn(
                GeneralModel(
                    code = Constant.ErrorCode.NO_ERROR,
                    data = AccountModel.StatusModel(
                        status = mockUserStatus
                    )
                )
            )

        AccountViewModel(mockRemoteModels, getTestDispatcher()).apply {
            this.liveData.observeForever(mockDataObserver)
            this.errorStatusLiveData.observeForever(mockErrorObserver)
            this.loadingStatusLiveData.observeForever(mockLoadingObserver)
            this.fetch()
        }

        Mockito.verify(mockLoadingObserver).onChanged(true)
        Mockito.verify(mockErrorObserver, Mockito.never()).onChanged(Mockito.anyString())
        Mockito.verify(mockDataObserver).onChanged(
            AccountModel.DisplayModel(
                name = mockUserName,
                title = mockUserTitle,
                status = mockUserStatus
            )
        )
        Mockito.verify(mockLoadingObserver).onChanged(false)
    }

    @Test
    fun test_fetch_with_timeout() = testingScope {
        Mockito.`when`(mockRemoteModels.getUserInfoModel(Constant.Proxy.ACCOUNT_INFO_MODEL))
            .thenReturn(
                GeneralModel(
                    code = Constant.ErrorCode.TIMEOUT,
                    data = null
                )
            )

        Mockito.lenient().`when`(
            mockRemoteModels.getStatusModel(
                mockUserName,
                Constant.Proxy.ACCOUNT_STATUS_MODEL
            )
        ).thenReturn(
            GeneralModel(
                code = Constant.ErrorCode.TIMEOUT,
                data = null
            )
        )

        AccountViewModel(mockRemoteModels, getTestDispatcher()).apply {
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
    fun test_fetch_with_request_exception_user_info() = testingScope {
        Mockito.`when`(mockRemoteModels.getUserInfoModel(Constant.Proxy.ACCOUNT_INFO_MODEL))
            .thenThrow(RuntimeException())

        AccountViewModel(mockRemoteModels, getTestDispatcher()).apply {
            this.liveData.observeForever(mockDataObserver)
            this.errorStatusLiveData.observeForever(mockErrorObserver)
            this.loadingStatusLiveData.observeForever(mockLoadingObserver)
            this.fetch()
        }

        Mockito.verify(mockLoadingObserver).onChanged(true)
        Mockito.verify(mockErrorObserver).onChanged(Mockito.anyString())
        Mockito.verify(mockDataObserver, Mockito.never()).onChanged(Mockito.any())
        Mockito.verify(mockLoadingObserver).onChanged(false)
    }

    @Test
    fun test_fetch_with_request_exception_user_status() = testingScope {
        Mockito.lenient().`when`(
            mockRemoteModels.getStatusModel(
                mockUserName,
                Constant.Proxy.ACCOUNT_STATUS_MODEL
            )
        ).thenThrow(RuntimeException())

        AccountViewModel(mockRemoteModels, getTestDispatcher()).apply {
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