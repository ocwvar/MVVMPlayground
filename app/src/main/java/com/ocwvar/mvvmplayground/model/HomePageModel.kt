package com.ocwvar.mvvmplayground.model

import com.ocwvar.mvvmplayground.base.GeneralModel
import com.ocwvar.mvvmplayground.general.Constant
import com.ocwvar.mvvmplayground.network.Remote

class HomePageModel(private val remoteModels: Remote.Models) {

    data class Model(val content: String)

    suspend fun getModel(): GeneralModel<Model> {
        return this.remoteModels.getHomeModel(Constant.Proxy.HOME_MODEL)
    }

}

