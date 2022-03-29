package com.ocwvar.mvvmplayground.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ocwvar.mvvmplayground.base.BaseViewModel
import com.ocwvar.mvvmplayground.model.HomePageModel
import com.ocwvar.mvvmplayground.network.Remote
import kotlinx.coroutines.CoroutineDispatcher

class HomeViewModel(
    remoteModels: Remote.Models,
    workerDispatcher: CoroutineDispatcher
) : BaseViewModel(workerDispatcher) {

    private val model: HomePageModel = HomePageModel(remoteModels)
    private val mLiveData: MutableLiveData<HomePageModel.Model> = MutableLiveData()

    val liveData: LiveData<HomePageModel.Model>
        get() = this.mLiveData

    /**
     * begin to fetch model
     */
    override fun fetch() = super.beginFetch {
        val responseModel = this.model.getModel()
        if (super.hasError(responseModel)) return@beginFetch

        this.mLiveData.postValue(responseModel.getData())
    }
}