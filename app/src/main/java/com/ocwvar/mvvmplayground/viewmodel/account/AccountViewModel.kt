package com.ocwvar.mvvmplayground.viewmodel.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ocwvar.mvvmplayground.base.BaseViewModel
import com.ocwvar.mvvmplayground.network.Remote
import com.ocwvar.mvvmplayground.model.AccountModel
import kotlinx.coroutines.CoroutineDispatcher

class AccountViewModel(
    remoteModels: Remote.Models,
    workerDispatcher: CoroutineDispatcher
) : BaseViewModel(workerDispatcher) {

    private val model: AccountModel = AccountModel(remoteModels)
    private val mLiveData: MutableLiveData<AccountModel.DisplayModel> = MutableLiveData()

    val liveData: LiveData<AccountModel.DisplayModel>
        get() = this.mLiveData

    /**
     * begin to fetch model
     */
    override fun fetch() = super.beginFetch {
        //request user model for it's user name
        val userModel = this.model.getUserInfoModel()
        if (super.hasError(userModel)) return@beginFetch

        //using user name to request user status
        val statusModel = this.model.getStatusModel(userModel.getData().name)
        if (super.hasError(statusModel)) return@beginFetch

        //assemble two model into a display model
        val displayModel = assembleDisplayModel(userModel.getData(), statusModel.getData())
        this.mLiveData.postValue(displayModel)
    }

    /**
     * assemble display model with [AccountModel.UserInfoModel] and [AccountModel.StatusModel]
     *
     * @return Display model that UI needs to show
     */
    private fun assembleDisplayModel(
        userInfoModel: AccountModel.UserInfoModel,
        statusModel: AccountModel.StatusModel
    ): AccountModel.DisplayModel {
        return AccountModel.DisplayModel(
            name = userInfoModel.name,
            title = userInfoModel.title,
            status = statusModel.status
        )
    }

}