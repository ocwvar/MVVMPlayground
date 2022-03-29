package com.ocwvar.mvvmplayground.model

import com.ocwvar.mvvmplayground.base.GeneralModel
import com.ocwvar.mvvmplayground.general.Constant
import com.ocwvar.mvvmplayground.network.Remote

class AccountModel(private val remoteModels: Remote.Models) {

    //data model
    data class UserInfoModel(val name: String, val title: String)
    data class StatusModel(val status: String)

    //final display model
    data class DisplayModel(val name: String, val title: String, val status: String)

    suspend fun getUserInfoModel(): GeneralModel<UserInfoModel> {
        return this.remoteModels.getUserInfoModel(Constant.Proxy.ACCOUNT_INFO_MODEL)
    }

    suspend fun getStatusModel(userName: String): GeneralModel<StatusModel> {
        return this.remoteModels.getStatusModel(
            userName = userName,
            proxy = Constant.Proxy.ACCOUNT_STATUS_MODEL
        )
    }

}